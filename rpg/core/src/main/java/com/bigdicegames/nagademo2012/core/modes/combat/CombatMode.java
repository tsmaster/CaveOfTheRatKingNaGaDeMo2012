package com.bigdicegames.nagademo2012.core.modes.combat;

import static playn.core.PlayN.log;

import java.util.ArrayList;

import playn.core.Key;
import playn.core.Mouse.ButtonEvent;
import playn.core.PlayN;
import playn.core.Pointer.Event;

import com.bigdicegames.nagademo2012.core.CameraMgr;
import com.bigdicegames.nagademo2012.core.Encounter;
import com.bigdicegames.nagademo2012.core.InventoryObject;
import com.bigdicegames.nagademo2012.core.InventoryObject.InventoryProto;
import com.bigdicegames.nagademo2012.core.MonsterPlacement;
import com.bigdicegames.nagademo2012.core.Party;
import com.bigdicegames.nagademo2012.core.characters.BaseCharacter;
import com.bigdicegames.nagademo2012.core.map.GameMap;
import com.bigdicegames.nagademo2012.core.map.Location;
import com.bigdicegames.nagademo2012.core.map.LongRunningJob;
import com.bigdicegames.nagademo2012.core.map.MapMgr;
import com.bigdicegames.nagademo2012.core.map.mapobjects.combat.Combatant;
import com.bigdicegames.nagademo2012.core.map.mapobjects.combat.Enemy;
import com.bigdicegames.nagademo2012.core.map.mapobjects.combat.PlayerCharacter;
import com.bigdicegames.nagademo2012.core.map.mapobjects.combat.brains.BrainFactory;
import com.bigdicegames.nagademo2012.core.math.Vec2i;
import com.bigdicegames.nagademo2012.core.modes.AlertMode;
import com.bigdicegames.nagademo2012.core.modes.GameMode;
import com.bigdicegames.nagademo2012.core.modes.ModeMgr;
import com.bigdicegames.nagademo2012.core.modes.SplashScreenMode;

public class CombatMode implements GameMode {
	private static final float DROP_PROBABILITY = 0.7f;
	private ArrayList<Enemy> enemies;
	private ArrayList<PlayerCharacter> playerCharacters;
	private WaitForCommand wfcMode;
	private WaitForAnim animMode;
	private String parentMapName;
	private Encounter encounter;

	public CombatMode(Encounter e) {
		wfcMode = new WaitForCommand();
		animMode = new WaitForAnim();
		this.encounter = e;
	}

	@Override
	public void onPopped() {
	}
	
	@Override
	public void onBecomeTop() {
	}

	@Override
	public void onPushed() {
		parentMapName = MapMgr.get().getCurrentMapName();
		MapMgr.get().goToMap(encounter.getMapName(), null);
		GameMap map = MapMgr.get().getCurrentMap();
		populateMap(map);
		
		// TODO work out initiative
	}

	private void populateMap(GameMap map) {
		enemies = new ArrayList<Enemy>();
		ArrayList<Vec2i> enemySpawnPoints = map.getEnemySpawnPoints();
		for (MonsterPlacement mp : encounter.getMonsters()) {
			Enemy e = new Enemy(mp.getIconFilename());
			e.setCharacterName(mp.getMonsterName())
			 .setHitPoints(mp.getHitPoints())
			 .setBrain(BrainFactory.makeBrain(mp.getBrain(), e));
			map.placeMapObject(e, getEmptyPosn(enemySpawnPoints));
			enemies.add(e);
		}
		
		playerCharacters = new ArrayList<PlayerCharacter>();
		ArrayList<Vec2i> playerSpawnPoints = map.getPlayerSpawnPoints();

		for (BaseCharacter cr : Party.get().getCharacters()) {
			if (cr.isAlive()) {
				playerCharacters.add(cr.makeGuy());
			}
		}
		
		for (int i = 0; i<playerCharacters.size(); ++i) {
			map.placeMapObject(playerCharacters.get(i), getEmptyPosn(playerSpawnPoints));
		}
	}

	private Vec2i getEmptyPosn(ArrayList<Vec2i> spawnPointList) {
		int startIndex = (int) (PlayN.random() * spawnPointList.size());
		
		GameMap map = MapMgr.get().getCurrentMap();
		Vec2i pos = spawnPointList.get(startIndex);
		Location loc = map.getLocation(pos);
		
		if (loc.getMapObjects().size() == 0) {
			return pos;
		}
		for (int offset = 0; offset < spawnPointList.size(); ++offset) {
			pos = spawnPointList.get((startIndex + offset) % spawnPointList.size());
			loc = map.getLocation(pos);
			if (loc.getMapObjects().size() == 0) {
				return pos;
			}
		}
		// TODO pick neighbor
		return null;
	}

	@Override
	public void onUpdate(float deltaSeconds) {
		CameraMgr.get().update(deltaSeconds);

		if (!enemiesAlive()) {
			if (playersAlive()) {
				// victory!
				GameMap map = MapMgr.get().getCurrentMap();
				map.clearMapObjects();
				MapMgr.get().goToMap(parentMapName, null);
				ModeMgr.get().popMode(this);
				encounter.setComplete(true);
				
				log().info("encounter is final?" + encounter.isFinal());
				if (encounter.isFinal()) {
					SplashScreenMode title = new SplashScreenMode("images/winScreen.png");
					ModeMgr.get().pushMode(title);
				}

				int gold = encounter.getGold();
				int experience = encounter.getExperience();
				ArrayList<InventoryObject> drops = generateDrops();
				
				Party.get().addGold(gold);
				Party.get().addExperience(experience);
				Party.get().addInventory(drops);
				
				String successText="You have prevailed over your enemies.\n \n";
				if (gold > 0) {
					successText += "You have earned "+gold+" gold.\n \n";
				}
				if (experience > 0) {
					successText += "You have earned "+experience+" experience.\n \n";
				}
				for (InventoryObject o : drops) {
					successText += "You have found a "+o.getName() +"\n";
				}
				AlertMode welcome = new AlertMode()
				.setAlertText(successText)
				.setBackgroundColor(0xff408040);
				welcome.render();
				ModeMgr.get().pushMode(welcome);
				return;
			}
		}
		
		if (!playersAlive()) {
			// failure!
			GameMap map = MapMgr.get().getCurrentMap();
			map.clearMapObjects();
			
			Party.get().respawn();
			MapMgr.get().goToMapAndRespawn(parentMapName);
			ModeMgr.get().popMode(this);
			
			String successText="Your party has been defeated. You limp back to camp.";
			AlertMode welcome = new AlertMode()
			.setAlertText(successText)
			.setBackgroundColor(0xff804040);
			welcome.render();
			ModeMgr.get().pushMode(welcome);
			return;
		}
		
		
		for (Combatant c : getCombatants()) {
			if (!c.isAlive()) {
				continue;
			}
			
			Command cmd = c.getCommand(); 
			if (cmd == null) {
				if (c.hasBrain()) {
					Vec2i activePos= c.getPosition();
					Location loc = MapMgr.get().getCurrentMap().getLocation(activePos);
					log().info("interpolating to "+activePos.toString());
					CameraMgr.get().interpolateTo(loc.getWorldPos(), 0.5f, CameraMgr.InterpolationStyle.EASE_OUT_QUAD);
					cmd = c.selectCombatAction(this);
					c.setCommand(cmd);
				} else {
					wfcMode.setActiveCharacter(c);
					ModeMgr.get().pushMode(wfcMode);
				}
				return;
			}
			LongRunningJob j = c.getAnimationJob(); 
			if (j != null && !j.isComplete()) {
				log().info("waiting for anim "+c);
				animMode.setActiveCharacter(c);
				ModeMgr.get().pushMode(animMode);
				return;
			}
		}
		
		// everybody's done, start over
		
		for (Combatant c : getCombatants()) {
			c.setCommand(null);
		}
	}

	private ArrayList<InventoryObject> generateDrops() {
		// TODO make this driven by encounter object
		ArrayList<InventoryObject> drops = new ArrayList<InventoryObject>();
		
		if (PlayN.random() < DROP_PROBABILITY) {
			drops.add(getRandomDrop());
		}
		return drops;
	}

	enum DropFreq {
		DROP_DAGGER(0.1f, InventoryObject.InventoryProto.INV_DAGGER), 
		DROP_SWORD(0.025f, InventoryObject.InventoryProto.INV_SWORD),
		DROP_AXE(0.05f, InventoryObject.InventoryProto.INV_AXE),
		DROP_BOW(0.05f, InventoryObject.InventoryProto.INV_BOW),
		DROP_MACE(0.05f, InventoryObject.InventoryProto.INV_MACE),
		DROP_STAFF(0.05f, InventoryObject.InventoryProto.INV_STAFF),
		DROP_CLOTH_ARMOR(0.06f, InventoryObject.InventoryProto.INV_CLOTH_ARMOR), 
		DROP_LEATHER(0.12f, InventoryObject.InventoryProto.INV_LEATHER_ARMOR),
		DROP_STUDDED(0.09f, InventoryObject.InventoryProto.INV_STUDDED_ARMOR), 
		DROP_CHAINMAIL(0.06f, InventoryObject.InventoryProto.INV_CHAIN_MAIL),
		DROP_PLATEMAIL(0.03f, InventoryObject.InventoryProto.INV_PLATE_MAIL);
		
		public float probability;
		public InventoryProto proto;

		DropFreq(float probability, InventoryObject.InventoryProto proto) {
			this.probability = probability;
			this.proto = proto;
		}
	};
	
	private InventoryObject getRandomDrop() {
		float totalFreq=0;
		for (DropFreq f : DropFreq.values()) {
			totalFreq += f.probability;
		}
		float roll = PlayN.random() * totalFreq;
		for (DropFreq f : DropFreq.values()) {
			roll -= f.probability;
			if (roll < 0) {
				return new InventoryObject(f.proto);
			}
		}
		return null;
	}

	private ArrayList<Combatant> getCombatants() {
		ArrayList<Combatant> ret = new ArrayList<Combatant>();
		
		ret.addAll(enemies);
		ret.addAll(playerCharacters);
		
		return ret;
	}

	@Override
	public void onPaint(float alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseDown(ButtonEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerStart(Event event) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Combatant> getEnemies(Combatant fighter) {
		ArrayList<Combatant> enemies = new ArrayList<Combatant>();
		
		for (Combatant c : getCombatants()) {
			if (fighter.opposed(c)) {
				enemies.add(c);
			}
		}
		return enemies;
	}

	private boolean enemiesAlive() {
		for (Combatant e : enemies) {
			if (e.isAlive()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean playersAlive() {
		for (Combatant e : playerCharacters) {
			if (e.isAlive()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {
		if (event.key() == Key.K) {
			// kill the enemies
			for (Combatant c : enemies) {
				int hp = c.getCurrentHitPoints();
				c.addHitPoints(-hp);
			}
		}
	}

	@Override
	public void onBecomeNotTop() {
		// TODO Auto-generated method stub
		
	}
	
}

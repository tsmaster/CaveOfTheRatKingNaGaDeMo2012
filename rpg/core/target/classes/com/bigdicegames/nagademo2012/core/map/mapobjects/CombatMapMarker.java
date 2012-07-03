package com.bigdicegames.nagademo2012.core.map.mapobjects;

import playn.core.PlayN;

import com.bigdicegames.nagademo2012.core.Encounter;
import com.bigdicegames.nagademo2012.core.MonsterPlacement;
import com.bigdicegames.nagademo2012.core.map.GameMap;
import com.bigdicegames.nagademo2012.core.map.MapObject;
import com.bigdicegames.nagademo2012.core.modes.ModeMgr;
import com.bigdicegames.nagademo2012.core.modes.combat.CombatMode;

public class CombatMapMarker extends MapObject {
	private Encounter encounter;

	public CombatMapMarker(String monsterName, String mapName, GameMap parentMap, boolean finalCombat) {
		super(null);

		int numRatMen = (int) (PlayN.random() * 4 + 1);
		int numRatKings = (int) (PlayN.random() * 2);
		
		if (monsterName.equals("rat king")) {
			setIcon("images/ratking_100.png");
			numRatKings = 1;
			numRatMen = 5;
		} else if (monsterName.equals("rat man")) {
			setIcon("images/ratguy_100.png");
			numRatKings = 0;
		}
		
		encounter = new Encounter()
			.setMapName(mapName)
			.setParentMap(parentMap)
			.setFinal(finalCombat)
			.setGold(30*numRatMen+100*numRatKings)
			.setExperience(200*numRatMen+500*numRatKings);
		
		for (int i = 0; i< numRatMen; ++i) {
			MonsterPlacement mp = new MonsterPlacement()
			   .setIconName("images/ratguy_100.png")
			   .setMonsterName("ratman soldier")
			   .setHitPoints(5)
			   .setBrain("attack closest");
			encounter.addMonster(mp);
		}
		
		for (int i = 0 ; i < numRatKings; ++i) {
			MonsterPlacement mp = new MonsterPlacement()
			   .setIconName("images/ratking_100.png")
			   .setMonsterName("King of the Rat Men")
			   .setHitPoints(20)
			   .setBrain("attack closest");
			encounter.addMonster(mp);
		}
	}

	@Override
	protected void isLandedOn(MapObject mapObject) {
		ModeMgr.get().pushMode(new CombatMode(encounter));
	}

	@Override
	public boolean stopsMovement() {
		return true;
	}

	public Encounter getEncounter() {
		return encounter;
	}
}
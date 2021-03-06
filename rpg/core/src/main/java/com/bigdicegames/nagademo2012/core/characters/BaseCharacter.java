package com.bigdicegames.nagademo2012.core.characters;

import static playn.core.PlayN.log;

import com.bigdicegames.nagademo2012.core.InventoryObject;
import com.bigdicegames.nagademo2012.core.InventoryObject.InventoryProto;
import com.bigdicegames.nagademo2012.core.map.mapobjects.combat.PlayerCharacter;

public class BaseCharacter {
	private String name;
	private int currentHitPoints;
	protected int experiencePoints;
	private InventoryProto weaponProto;
	private InventoryProto armorProto;

	public BaseCharacter() {
		resetHitPoints();
	}
	
	public BaseCharacter setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public String getClassName() {
		return "Base";
	}
	
	public void addHitPoints(int hitPoints) {
		this.currentHitPoints = Math.min(getMaxHitPoints(), Math.max(0, currentHitPoints+hitPoints));
	}
	
	public void addExperiencePoints(int expPoints) {
		int oldLevel = getLevel();
		this.experiencePoints += expPoints;
		int newLevel = getLevel();
		if (newLevel > oldLevel) {
			resetHitPoints();
		}
		log().info(name + " Exp: " + experiencePoints + " Lvl: " + newLevel);
	}

	public void resetHitPoints() {
		currentHitPoints = getMaxHitPoints();
	}

	public boolean isAlive() {
		return currentHitPoints > 0;
	}
	
	public int getMaxHitPoints() {
		return 10;
	}
	
	public int getCurrentHitPoints() {
		return currentHitPoints;
	}
	
	public int getLevel() {
		return experiencePoints / 1000 + 1;
	}
	
	public int getToHit() {
		return 10;
	}
	
	public int getSpellPoints() {
		return 0;
	}

	public PlayerCharacter makeGuy() {
		PlayerCharacter pc;
		pc = new PlayerCharacter(this);
		pc.setCharacterName(getName());
		return pc;
	}

	public String getIconFilename() {
		return "images/guy_100.png";
	}

	public int getExperiencePoints() {
		return experiencePoints;
	}

	public InventoryObject.InventoryProto getWeaponProto() {
		return weaponProto;
	}
	
	public InventoryObject.InventoryProto getArmorProto() {
		return armorProto;
	}

	public BaseCharacter setWeaponProto(InventoryProto newProto) {
		weaponProto = newProto;
		return this;
	}

	public BaseCharacter setArmorProto(InventoryProto newProto) {
		armorProto = newProto;
		return this;
	}
}

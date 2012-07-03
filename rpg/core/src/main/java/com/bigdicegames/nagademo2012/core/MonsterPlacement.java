package com.bigdicegames.nagademo2012.core;

import java.util.ArrayList;

public class MonsterPlacement {
	String monsterName;
	String iconFilename;
	int hitPoints;
	int startingX;
	int startingY;
	String brain;
	int gold;
	ArrayList<InventoryObject> loot;
	
	public MonsterPlacement() {
		loot = new ArrayList<InventoryObject>();
	}
	
	
	public MonsterPlacement setIconName(String filename) {
		iconFilename = filename;
		return this;
	}
	
	public String getIconFilename() {
		return iconFilename;
	}

	public MonsterPlacement setMonsterName(String name) {
		monsterName = name;
		return this;
	}
	
	public String getMonsterName() {
		return monsterName;
	}

	public MonsterPlacement setHitPoints(int hp) {
		hitPoints = hp;
		return this;
	}
	
	public int getHitPoints() {
		return hitPoints;
	}

	public MonsterPlacement setBrain(String brain) {
		this.brain = brain;
		return this;
	}
	
	public String getBrain() {
		return brain;
	}

}
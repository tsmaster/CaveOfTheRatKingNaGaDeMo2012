package com.bigdicegames.nagademo2012.core;

import java.util.ArrayList;

import com.bigdicegames.nagademo2012.core.map.GameMap;

public class Encounter {
	boolean isComplete;
	String mapName;
	ArrayList<MonsterPlacement> monsters;
	private GameMap parentMap;
	private boolean finalCombat;
	private int gold;
	private int experience;
	
	public Encounter() {
		monsters = new ArrayList<MonsterPlacement>();
	}
	
	public Encounter addMonster(MonsterPlacement mp) {
		monsters.add(mp);
		return this;
	}
	
	public ArrayList<MonsterPlacement> getMonsters() {
		return monsters;
	}

	public String getMapName() {
		return mapName;
	}

	public boolean isComplete() {
		return isComplete;
	}
	
	public void setComplete(boolean complete) {
		isComplete = complete;
		parentMap.updateEncounters();
	}

	public Encounter setMapName(String mapName) {
		this.mapName = mapName;
		return this;
	}
	
	public Encounter setParentMap(GameMap map) {
		this.parentMap = map;
		return this;
	}
	
	public GameMap getParentMap() {
		return parentMap;
	}

	public Encounter setFinal(boolean finalCombat) {
		this.finalCombat = finalCombat;
		return this;
	}
	
	public boolean isFinal() {
		return finalCombat;
	}

	public Encounter setGold(int gold) {
		this.gold = gold;
		return this;
	}
	public int getGold() {
		return gold;
	}

	public int getExperience() {
		return experience;
	}

	public Encounter setExperience(int experience) {
		this.experience = experience;
		return this;
	}

}

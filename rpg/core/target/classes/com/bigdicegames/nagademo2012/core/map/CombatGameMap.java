package com.bigdicegames.nagademo2012.core.map;

public class CombatGameMap extends GameMap {

	public CombatGameMap(String filename) {
		super(filename);
	}
	
	public MapObject getPartyMarker() {
		return null;
	}

	@Override
	protected void makePartyMarker() {
		
	}
}

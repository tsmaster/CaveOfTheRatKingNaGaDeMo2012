package com.bigdicegames.nagademo2012.core.map.mapobjects;

import com.bigdicegames.nagademo2012.core.map.MapMgr;
import com.bigdicegames.nagademo2012.core.map.MapObject;
import com.bigdicegames.nagademo2012.core.math.Vec2i;

public class PortalMapObject extends MapObject {

	private String name;
	private Vec2i exitTilePos;

	public PortalMapObject(String filename) {
		super(filename);
		// TODO Auto-generated constructor stub
	}

	public PortalMapObject(String name, int x, int y) {
		super(null);
		this.name = name;
		this.exitTilePos = new Vec2i(x,y);
	}
	
	@Override
	protected void isLandedOn(MapObject partyObject) {
		MapMgr.get().goToMap(name, exitTilePos);
	}
	
	@Override
	public boolean stopsMovement() {
		return true;
	}
}

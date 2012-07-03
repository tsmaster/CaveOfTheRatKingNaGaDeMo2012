package com.bigdicegames.nagademo2012.core.map.mapobjects;

import com.bigdicegames.nagademo2012.core.map.MapMgr;
import com.bigdicegames.nagademo2012.core.map.MapObject;
import com.bigdicegames.nagademo2012.core.math.Vec2i;

public class RatKing extends MapObject {
	public RatKing() {
		super("images/ratking_100.png");
	}
	
	@Override
	protected void isLandedOn(MapObject mapObject) {
		// TODO trigger combat
		
		MapMgr.get().goToMap("combat grass", new Vec2i(5,5));
	}

	@Override
	public boolean stopsMovement() {
		return true;
	}
}

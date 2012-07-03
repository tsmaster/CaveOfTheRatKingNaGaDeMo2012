package com.bigdicegames.nagademo2012.core.map.mapobjects;

import static playn.core.PlayN.log;

import com.bigdicegames.nagademo2012.core.map.MapObject;
import com.bigdicegames.nagademo2012.core.modes.HealerMode;
import com.bigdicegames.nagademo2012.core.modes.ModeMgr;

public class HealerMapObject extends MapObject {
	private String name;

	public HealerMapObject(String shopName) {
		super(null);
		this.name = shopName;
	}
	
	@Override
	protected void isLandedOn(MapObject partyObject) {
		log().info("healer landed on");
		ModeMgr.get().pushMode(new HealerMode());
	}
	
	@Override
	public boolean stopsMovement() {
		return true;
	}
}

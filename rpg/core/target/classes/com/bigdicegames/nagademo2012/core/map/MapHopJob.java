package com.bigdicegames.nagademo2012.core.map;

import com.bigdicegames.nagademo2012.core.CameraMgr;
import com.bigdicegames.nagademo2012.core.math.Vec2f;


public class MapHopJob implements LongRunningJob {
	private Location startLoc;
	private Location destLoc;
	private float elapsed;
	private Vec2f objectWorldPos;
	private Vec2f prevObjectWorldPos;
	private boolean isComplete;
	private MapObject object;
	
	private static final float HERMITE_SPEED = 1.2f;
	private static final float HERMITE_Y_START = -200.0f;
	private static final float HERMITE_Y_END = 200.0f;

	public MapHopJob(Location start, Location dest, MapObject obj) {
		this.startLoc = start;
		this.destLoc = dest;
		elapsed = 0.0f;

		this.object = obj;
		GameMap gameMap = MapMgr.get().getCurrentMap();
		gameMap.removeMapObject(obj);
		gameMap.addMovingMapObject(obj);
		objectWorldPos = startLoc.getWorldPos();
		prevObjectWorldPos = objectWorldPos;
		isComplete = false;
	}

	/**
	 * moves the object on the map
	 * @param deltaSeconds
	 * @return whether the job is complete
	 */
	public void update(float deltaSeconds) {
		CameraMgr.get().update(deltaSeconds);
		Vec2f camPos = CameraMgr.get().getPos();
		MapMgr.get().getCurrentMap().setCameraPosition(camPos.x, camPos.y);
		
		elapsed += deltaSeconds * HERMITE_SPEED;
		prevObjectWorldPos = objectWorldPos;
		
		if (elapsed > 1.0f) {
			elapsed = 1.0f;
		}
		
		Vec2f startPos = startLoc.getWorldPos();
		Vec2f destPos = destLoc.getWorldPos();
		float guyX = evalHermite(startPos.x, destPos.x, 0.0f, 0.0f, elapsed);
		float guyY = evalHermite(startPos.y, destPos.y, HERMITE_Y_START, HERMITE_Y_END, elapsed);
		objectWorldPos = new Vec2f(guyX, guyY);
	  
		if (elapsed >= 1.0f) {
			GameMap gameMap = MapMgr.get().getCurrentMap();
			gameMap.removeMapObject(object);
			gameMap.placeMapObject(object, destLoc.getTileX(), destLoc.getTileY());
			isComplete = true;
		}
	}
	
	public boolean isComplete() {
		return isComplete;
	}

	private float evalHermite(float p0, float p1, float m0, float m1, float t) {
		   float t2 = t*t;
		   float t3 = t2*t;
		   return (2*t3 - 3*t2 + 1)*p0 + (t3-2*t2+t)*m0 + (-2*t3+3*t2)*p1 + (t3-t2)*m1;
	}

	@Override
	public void onPaint(float alpha) {
		float x = (objectWorldPos.x * alpha) + prevObjectWorldPos.x * (1 - alpha);
		float y = (objectWorldPos.y * alpha) + prevObjectWorldPos.y * (1 - alpha);
		
		object.centerOnWorldPosition(x,y);
	}
	
}

package com.bigdicegames.nagademo2012.core.map;

import java.util.ArrayList;

import com.bigdicegames.nagademo2012.core.CameraMgr;
import com.bigdicegames.nagademo2012.core.Encounter;
import com.bigdicegames.nagademo2012.core.modes.ModeMgr;
import com.bigdicegames.nagademo2012.core.modes.combat.CombatMode;
import static playn.core.PlayN.log;

public class MultiHopJob implements LongRunningJob {
	
	private static final float CAMERA_INTERP_DURATION = 2.0f;
	private PathFindJob pathFindJob;
	private MapHopJob mapHopJob;
	private ArrayList<Location> path;
	private boolean isComplete;
	private Location finalLocation;
	private MapObject object;

	public MultiHopJob(Location startLoc, Location destLoc, MapObject object) {
		pathFindJob = new PathFindJob(startLoc, destLoc);
		isComplete = false;
		finalLocation = startLoc;
		this.object = object; 
	}

	@Override
	public void update(float deltaSeconds) {
		if (pathFindJob != null) {
			pathFindJob.update(deltaSeconds);
			if (pathFindJob.isComplete()) {
				path = pathFindJob.getPath();
				if (path == null) {
					isComplete = true;
				}
				pathFindJob = null;
				
				CameraMgr camMgr = CameraMgr.get();
				if (path != null) {
					camMgr.interpolateTo(path.get(path.size()-1).getWorldPos(), CAMERA_INTERP_DURATION, CameraMgr.InterpolationStyle.EASE_OUT_QUAD);
				}
			}
		}
		if (path != null) {
			if (mapHopJob != null) {
				mapHopJob.update(deltaSeconds);
				if (mapHopJob.isComplete()) {
					mapHopJob = null;
					Location loc = path.get(0);
					if (loc.mapObjects != null && loc.mapObjects.size() > 0) {
						for (MapObject obj: loc.mapObjects) {
							MapMgr.get().getCurrentMap().getFocusedMarker().landOn(obj);
							if (obj.stopsMovement()) {
								isComplete = true;
								finalLocation = loc;
								path = null;
								return;
							}
						}
					}
					log().info("rolling random encounter");
					Encounter e = MapMgr.get().getCurrentMap().getRandomEncounter(); 
					if (e != null) {
						CombatMode cm = new CombatMode(e);
						ModeMgr.get().pushMode(cm);
						isComplete = true;
						finalLocation = loc;
						path = null;
						return;
					}
				}
			} else {
				if (path.size() > 1) {
					mapHopJob = new MapHopJob(path.get(0), path.get(1), object);
					path.remove(0);
				} else {
					isComplete = true;
					finalLocation = path.get(0);
				}
			}
		}
	}

	@Override
	public boolean isComplete() {
		return isComplete;
	}

	public void onPaint(float alpha) {
		if (mapHopJob != null) {
			mapHopJob.onPaint(alpha);
		}
	}

	public Location finalLocation() {
		return finalLocation;
	}
}

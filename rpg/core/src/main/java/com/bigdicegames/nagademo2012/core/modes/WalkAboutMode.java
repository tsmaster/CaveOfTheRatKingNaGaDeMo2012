package com.bigdicegames.nagademo2012.core.modes;

import static playn.core.PlayN.graphics;
import playn.core.Key;
import playn.core.Mouse.ButtonEvent;
import playn.core.Pointer.Event;

import com.bigdicegames.nagademo2012.core.CameraMgr;
import com.bigdicegames.nagademo2012.core.Party;
import com.bigdicegames.nagademo2012.core.map.GameMap;
import com.bigdicegames.nagademo2012.core.map.Location;
import com.bigdicegames.nagademo2012.core.map.MapMgr;
import com.bigdicegames.nagademo2012.core.map.MultiHopJob;
import com.bigdicegames.nagademo2012.core.math.Vec2f;
import com.bigdicegames.nagademo2012.core.math.Vec2i;
import com.bigdicegames.nagademo2012.core.ui.PushButton;
import com.bigdicegames.nagademo2012.core.ui.PushButton.ButtonCallback;
import com.bigdicegames.nagademo2012.core.util.BoundingBox4f;
import com.bigdicegames.nagademo2012.core.util.BoundingBox4i;

public class WalkAboutMode implements GameMode {
	private Vec2i destPos;
	private MultiHopJob multiHopJob;
	private PushButton partyButton; 

	public WalkAboutMode() {
		partyButton = new PushButton("Party", new BoundingBox4i(0,0,80,40), new ButtonCallback(){
			@Override
			public void onClicked() {
				ModeMgr.get().pushMode(new PartyStatsMode());
			}});
	}

	@Override
	public void onPopped() {
		graphics().rootLayer().remove(partyButton.getLayer());
	}
	
	@Override
	public void onBecomeTop() {
		graphics().rootLayer().add(partyButton.getLayer());
	}
	
	@Override
	public void onBecomeNotTop() {
		graphics().rootLayer().remove(partyButton.getLayer());
	}

	@Override
	public void onPushed() {
	}

	@Override
	public void onUpdate(float deltaSeconds) {
		GameMap gameMap = MapMgr.get().getCurrentMap();
		if (gameMap == null) {
			return;
		}
		
		BoundingBox4f box = gameMap.getBoundingBox();
		CameraMgr camMgr = CameraMgr.get();
		
		if (box == null) {
			return;
		}
		camMgr.update(deltaSeconds);
		Vec2f camPos = camMgr.getPos();
		gameMap.setCameraPosition(camPos.x, camPos.y);
		
		if (multiHopJob != null) {
			multiHopJob.update(deltaSeconds);
			if (multiHopJob.isComplete()) {
				gameMap.setPlayerPos(multiHopJob.finalLocation().getTilePos());
				multiHopJob = null;
			}
		}
	}

	@Override
	public void onPaint(float alpha) {
		if (multiHopJob != null) {
			multiHopJob.onPaint(alpha);
		}
	}

	@Override
	public void onMouseDown(ButtonEvent event) {
		/*
		float sx = event.localX();
		float sy = event.localY();
		moveTo(new Vec2f(sx, sy));*/
	}

	@Override
	public void onPointerStart(Event event) {
		if (partyButton.tryClick(event)) {
			return;
		}
		float sx = event.localX();
		float sy = event.localY();
		moveTo(new Vec2f(sx, sy));
	}
	
	private void moveTo(Vec2f screenPos) {
		if (multiHopJob != null) {
			return;
		}
		
		GameMap gameMap = MapMgr.get().getCurrentMap();
		Vec2i myPos = gameMap.getPlayerPos();
		
		Vec2f worldPos = gameMap.screenToWorld(screenPos.x, screenPos.y);
		destPos = GameMap.worldToTile(worldPos);
		Location startLoc = gameMap.getLocation(myPos.x, myPos.y);
		Location destLoc = gameMap.getLocation(destPos.x, destPos.y);

		/*
		hopJob = new MapHopJob(gameMap, myGuy, startLoc, destLoc);
		pathFindJob = new PathFindJob(startLoc, destLoc);*/
		multiHopJob = new MultiHopJob(startLoc, destLoc, MapMgr.get().getCurrentMap().getFocusedMarker());
	}

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {
		if (event.key() == Key.P) {
			Party.get().logStatus();
			ModeMgr.get().pushMode(new PartyStatsMode());
		}
	}

}

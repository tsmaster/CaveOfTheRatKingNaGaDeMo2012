package com.bigdicegames.nagademo2012.core.modes;

import playn.core.Mouse.ButtonEvent;
import playn.core.Pointer.Event;

import com.bigdicegames.nagademo2012.core.map.GameMap;
import static playn.core.PlayN.*;

public class RandomTourMode implements GameMode {

	private static final float SPEED = 100; // pixels per second
	private GameMap gameMap;
	private float camX;
	private float prevCamX;
	private float camY;
	private float prevCamY;
	private float destX;
	private float destY;

	public RandomTourMode(GameMap gameMap) {
		this.gameMap = gameMap;
	}

	@Override
	public void onPopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPushed() {
		pickRandomDest();
	}
	
	@Override
	public void onBecomeTop() {
		
	}

	@Override
	public void onUpdate(float deltaSeconds) {
	  prevCamX = camX;
	  prevCamY = camY;
	  
	  float diffX = destX - camX;
	  float diffY = destY - camY;
	  // TODO get rid of sqrt
	  float diffH = (float) Math.sqrt(diffX * diffX + diffY * diffY);

	  if (diffH > deltaSeconds * SPEED) {
		  camX += diffX/diffH * deltaSeconds * SPEED;
		  camY += diffY/diffH * deltaSeconds * SPEED;
		  
		  //gameMap.setCameraPosition(camX, camY);
		  
	  } else {
		  pickRandomDest();
		  log().info("new dest: "+destX+","+destY);
	  }
	}

	private void pickRandomDest() {
		  destX = gameMap.getRandomX();
		  destY = gameMap.getRandomY();
	}

	@Override
	public void onPaint(float alpha) {
		float x = (camX * alpha) + prevCamX * (1 - alpha);
		float y = (camY * alpha) + prevCamY * (1 - alpha);
		
		gameMap.setCameraPosition(x, y);
	}

	@Override
	public void onMouseDown(ButtonEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerStart(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBecomeNotTop() {
		// TODO Auto-generated method stub
		
	}

}

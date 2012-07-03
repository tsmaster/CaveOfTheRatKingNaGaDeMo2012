package com.bigdicegames.nagademo2012.core.modes;

import java.util.ArrayList;

import playn.core.Mouse.ButtonEvent;
import playn.core.Pointer;

public class ModeMgr {
	static ModeMgr singleton;
	private ArrayList<GameMode> modeStack = new ArrayList<GameMode>();
	
	private ModeMgr() {
		singleton = this;
	}
	
	public void pushMode(GameMode mode) {
		GameMode oldTop = getTopMode();
		if (oldTop!=null) {
			oldTop.onBecomeNotTop();
		}
		
		modeStack.add(mode);
		mode.onPushed();
		mode.onBecomeTop();
	}
	
	public void popMode(GameMode mode) {
		if (mode == null) {
			mode = getTopMode();
		}
		mode.onPopped();
		modeStack.remove(mode);
		
		mode = getTopMode();
		if (mode != null) {
			mode.onBecomeTop();
		}
	}
	
	public void clearModeStack() {
		// TODO maybe pop them in reverse order?
		for (GameMode m : modeStack) {
			m.onPopped();
		}
		modeStack.clear();
	}
	
	private GameMode getTopMode() {
		if (modeStack.size() == 0) {
			return null;
		}
		return modeStack.get(modeStack.size() - 1);
	}
	
	public void onUpdate(float deltaSeconds) {
		GameMode mode = getTopMode();
		if (mode != null) {
			mode.onUpdate(deltaSeconds);
		}
	}

	public void onPaint(float alpha) {
		GameMode mode = getTopMode();
		if (mode != null) {
			mode.onPaint(alpha);
		}
	}
	
	
	public static ModeMgr get() {
		if (singleton == null) {
			singleton = new ModeMgr();
		}
		return singleton;
	}

	public void onMouseDown(ButtonEvent event) {
		GameMode mode = getTopMode();
		if (mode != null) {
			mode.onMouseDown(event);
		}
	}

	public void onPointerStart(Pointer.Event event) {
		GameMode mode = getTopMode();
		if (mode != null) {
			mode.onPointerStart(event);
		}
	}

	public void onKeyDown(playn.core.Keyboard.Event event) {
		GameMode mode = getTopMode();
		if (mode != null) {
			mode.onKeyDown(event);
		}
	}
}

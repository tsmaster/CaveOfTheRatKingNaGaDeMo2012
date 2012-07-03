package com.bigdicegames.nagademo2012.core.modes;

import playn.core.Keyboard.Event;
import playn.core.Mouse.ButtonEvent;
import playn.core.Pointer;

public interface GameMode {

	void onPopped();

	void onPushed();

	void onUpdate(float deltaSeconds);

	void onPaint(float alpha);

	void onMouseDown(ButtonEvent event);

	void onPointerStart(Pointer.Event event);

	void onBecomeTop();

	void onKeyDown(Event event);

	void onBecomeNotTop();

}

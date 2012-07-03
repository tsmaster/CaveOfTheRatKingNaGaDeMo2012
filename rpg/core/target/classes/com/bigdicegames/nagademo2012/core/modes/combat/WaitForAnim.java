package com.bigdicegames.nagademo2012.core.modes.combat;

import playn.core.Mouse.ButtonEvent;
import playn.core.Pointer.Event;

import com.bigdicegames.nagademo2012.core.map.LongRunningJob;
import com.bigdicegames.nagademo2012.core.map.mapobjects.combat.Combatant;
import com.bigdicegames.nagademo2012.core.modes.GameMode;
import com.bigdicegames.nagademo2012.core.modes.ModeMgr;

public class WaitForAnim implements GameMode {

	private Combatant object;

	@Override
	public void onPopped() {
	}

	@Override
	public void onPushed() {
	}
	
	@Override
	public void onBecomeTop() {
		
	}

	@Override
	public void onUpdate(float deltaSeconds) {
		LongRunningJob anim = object.getAnimationJob();
		if (anim == null || anim.isComplete()) {
			ModeMgr.get().popMode(this);
			return;
		}

		anim.update(deltaSeconds);
	}

	@Override
	public void onPaint(float alpha) {
		object.getAnimationJob().onPaint(alpha);
	}

	@Override
	public void onMouseDown(ButtonEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerStart(Event event) {
		// TODO Auto-generated method stub
		
	}

	public void setActiveCharacter(Combatant c) {
		this.object = c;
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

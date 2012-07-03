package com.bigdicegames.nagademo2012.core.modes.combat;

import com.bigdicegames.nagademo2012.core.math.Vec2i;
import com.bigdicegames.nagademo2012.core.modes.combat.WaitForCommand.Action;

public class Command {
	
	private Action action;
	private Vec2i destPos;

	public Command(Action action) {
		this.action = action;
	}

	public Command setDestination(Vec2i destPos) {
		this.destPos = destPos;
		return this;
	}
	
	public Vec2i getDestination() {
		return destPos;
	}

	public Command setAction(Action action) {
		this.action = action;
		return this;
	}
	
	public Action getAction() {
		return action;
	}

}

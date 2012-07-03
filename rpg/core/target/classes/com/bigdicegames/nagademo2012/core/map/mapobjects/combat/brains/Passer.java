package com.bigdicegames.nagademo2012.core.map.mapobjects.combat.brains;

import com.bigdicegames.nagademo2012.core.map.mapobjects.combat.Combatant;
import com.bigdicegames.nagademo2012.core.modes.combat.CombatMode;
import com.bigdicegames.nagademo2012.core.modes.combat.Command;
import com.bigdicegames.nagademo2012.core.modes.combat.WaitForCommand;

public class Passer implements CombatBrain {
	public Passer(Combatant body) {
		
	}
	
	@Override
	public Command selectCommand(CombatMode combatMode) {
		return new Command(WaitForCommand.Action.PASS);
	}

}

package com.bigdicegames.nagademo2012.core.map.mapobjects.combat.brains;

import com.bigdicegames.nagademo2012.core.modes.combat.CombatMode;
import com.bigdicegames.nagademo2012.core.modes.combat.Command;

public interface CombatBrain {
	Command selectCommand(CombatMode combatMode);
}

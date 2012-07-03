package com.bigdicegames.nagademo2012.core.map.mapobjects.combat.brains;

import com.bigdicegames.nagademo2012.core.map.mapobjects.combat.Combatant;

public class BrainFactory {

	public static CombatBrain makeBrain(String name, Combatant body) {
		if (name != null && name.equalsIgnoreCase("attack closest")) {
			return new AttackClosestEnemy(body);
		}
		return new Passer(body);
	}
}

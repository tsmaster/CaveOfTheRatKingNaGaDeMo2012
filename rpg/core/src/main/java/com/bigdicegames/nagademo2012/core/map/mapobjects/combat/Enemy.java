package com.bigdicegames.nagademo2012.core.map.mapobjects.combat;


public class Enemy extends Combatant {

	public Enemy(String filename) {
		super(filename);
		setAffiliation(Affiliation.HOSTILE)
			.setToHit(15)
			.setArmorClass(3)
			.setDamageDieSize(4);
	}

}

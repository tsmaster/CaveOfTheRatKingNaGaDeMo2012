package com.bigdicegames.nagademo2012.core.map.mapobjects.combat;

import com.bigdicegames.nagademo2012.core.characters.BaseCharacter;
import com.bigdicegames.nagademo2012.core.map.MapMgr;

public class PlayerCharacter extends Combatant {
	private BaseCharacter character;

	public PlayerCharacter(BaseCharacter cr) {
		super(cr.getIconFilename());
		setAffiliation(Affiliation.PLAYER);
		this.character = cr;
	}
	
	@Override
	public int getToHit() {
		return character.getToHit();
	}
	
	@Override
	public int getArmorClass() {
		return character.getArmorProto().armorClass;
	}

	@Override
	public int getDamageDieSize() {
		return character.getWeaponProto().damageDie;
	}
	
	@Override
	public int getBaseHitPoints() {
		return character.getMaxHitPoints();
	}
	
	@Override
	public int getCurrentHitPoints() {
		return character.getCurrentHitPoints();
	}

	@Override
	public void addHitPoints(int hitPoints) {
		character.addHitPoints(hitPoints);
		if (!character.isAlive()) {
			MapMgr.get().getCurrentMap().notifyDeath(this);
		}
	}

	@Override
	public boolean isAlive() {
		return character.isAlive();
	}
	
	
}

package com.bigdicegames.nagademo2012.core.characters;


public class FighterCharacter extends BaseCharacter {
	@Override
	public int getMaxHitPoints() {
		return 10 * getLevel();
	}

	@Override
	public int getToHit() {
		return 9 + getLevel();
	}

	@Override
	public String getClassName() {
		return "Fighter";
	}

	@Override
	public String getIconFilename() {
		return "images/fighter_100.png";
	}
	
}

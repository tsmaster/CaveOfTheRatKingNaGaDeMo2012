package com.bigdicegames.nagademo2012.core.characters;


public class MageCharacter extends BaseCharacter {
	@Override
	public int getMaxHitPoints() {
		return 6 * getLevel();
	}
	
	@Override
	public int getToHit() {
		return 10 + getLevel() / 2;
	}

	@Override
	public String getClassName() {
		return "Mage";
	}
	
	@Override
	public int getSpellPoints() {
		return 4 + 2 * getLevel();
	}
	
	@Override
	public String getIconFilename() {
		return "images/mage_100.png";
	}
	
}

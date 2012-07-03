package com.bigdicegames.nagademo2012.core;

import java.util.ArrayList;

public class Campaign {
	ArrayList<Encounter> scriptedEncounters;
	
	public void addEncounter(Encounter e) {
		scriptedEncounters.add(e);
	}
	
	public ArrayList<Encounter> getEncounters() {
		return scriptedEncounters;
	}
}

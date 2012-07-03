package com.bigdicegames.nagademo2012.core.map;

public class EncounterDescription {

	private String name;
	private String mapName;
	private double frequency;

	public EncounterDescription setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public String getMapName() {
		return mapName;
	}
	
	public EncounterDescription setMapName(String mapName) {
		this.mapName = mapName;
		return this;
	}

	public EncounterDescription setFrequency(double frequency) {
		this.frequency = frequency;
		return this;
	}

	public double getFrequency() {
		return frequency;
	}
}

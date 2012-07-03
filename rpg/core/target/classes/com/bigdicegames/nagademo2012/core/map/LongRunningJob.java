package com.bigdicegames.nagademo2012.core.map;

public interface LongRunningJob {
	public void update(float deltaSeconds);
	
	public boolean isComplete();

	public void onPaint(float alpha);
}

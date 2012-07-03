package com.bigdicegames.nagademo2012.core.ui;

import playn.core.ImageLayer;

public interface Widget {
	public ImageLayer getLayer();
	
	public Widget setBackgroundColor(int color);
	
	public Widget setForegroundColor(int color);
}

package com.bigdicegames.nagademo2012.core.math.interpolate;


public class LinearInterpolator implements Interpolator {

	@Override
	public float eval(float t, float b, float c, float d) {
		  return c*(t/d) + b;
	}

}

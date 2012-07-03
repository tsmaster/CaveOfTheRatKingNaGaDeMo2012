package com.bigdicegames.nagademo2012.core.math.interpolate;


public class EaseInInterpolator implements Interpolator {

	@Override
	public float eval(float t, float b, float c, float d) {
		  float n = t/d;
		  return c*n*n + b;
	}

}

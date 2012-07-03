package com.bigdicegames.nagademo2012.core.math.interpolate;

public class EaseOutInterpolator implements Interpolator {

	@Override
	public float eval(float t, float b, float c, float d) {
		  float n = t/d;
		  return -c*n*(n-2) + b;
	}

}

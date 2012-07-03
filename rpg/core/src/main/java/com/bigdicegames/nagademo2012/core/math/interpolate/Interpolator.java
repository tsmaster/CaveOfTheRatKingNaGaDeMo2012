package com.bigdicegames.nagademo2012.core.math.interpolate;

public interface Interpolator {
	
	// curves taken from http://www.robertpenner.com/easing/penner_chapter7_tweening.pdf
	// See also http://www.the-art-of-web.com/css/timing-function/
	// See also http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.3.js
	
	float eval(float t, float b, float c, float d);
}

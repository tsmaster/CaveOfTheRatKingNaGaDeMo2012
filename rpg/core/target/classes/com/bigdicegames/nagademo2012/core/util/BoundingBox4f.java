package com.bigdicegames.nagademo2012.core.util;

public class BoundingBox4f {
	public float left, right, top, bottom;
	
	public BoundingBox4f(float left, float top, float right, float bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public boolean intersects(BoundingBox4f other) {
		if (right < other.left ||
			left > other.right ||
			top > other.bottom ||
			bottom < other.top) { return false; }
		return true;
	}

	public BoundingBox4f translate(float x, float y) {
		return new BoundingBox4f(left + x,
				top + y,
				right + x,
				bottom + y);
	}

}

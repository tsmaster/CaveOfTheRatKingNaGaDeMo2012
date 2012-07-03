package com.bigdicegames.nagademo2012.core.util;

public class BoundingBox4i {
	public int left, right, top, bottom;
	
	public BoundingBox4i(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public boolean intersects(BoundingBox4i other) {
		if (right < other.left ||
			left > other.right ||
			top > other.bottom ||
			bottom < other.top) { return false; }
		return true;
	}

	public BoundingBox4i translate(int x, int y) {
		return new BoundingBox4i(left + x,
				top + y,
				right + x,
				bottom + y);
	}
	
	public int width() {
		return right - left;
	}
	
	public int height() {
		return bottom - top;
	}
}

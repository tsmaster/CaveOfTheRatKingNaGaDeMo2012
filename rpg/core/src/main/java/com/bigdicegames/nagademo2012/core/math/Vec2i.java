package com.bigdicegames.nagademo2012.core.math;

public class Vec2i {
	public int x, y;
	
	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+")";
	}
	
	public boolean equals(Vec2i o) {
		return this.x == o.x && this.y == o.y;
	}
}

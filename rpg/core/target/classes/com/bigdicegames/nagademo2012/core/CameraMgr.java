package com.bigdicegames.nagademo2012.core;

import com.bigdicegames.nagademo2012.core.math.Vec2f;
import com.bigdicegames.nagademo2012.core.math.interpolate.EaseInInterpolator;
import com.bigdicegames.nagademo2012.core.math.interpolate.EaseOutInterpolator;
import com.bigdicegames.nagademo2012.core.math.interpolate.Interpolator;
import com.bigdicegames.nagademo2012.core.math.interpolate.LinearInterpolator;

public class CameraMgr {
  private static CameraMgr singleton;
  
  public enum InterpolationStyle {
	  LINEAR,
	  EASE_IN_QUAD,  // start slow, speed up
	  EASE_OUT_QUAD,   // start fast, slow down
	  EASE_IN_OUT,
	  NONE,
  }
  
  private Vec2f cameraPos;
  private Vec2f startPos;
  private float elapsed;
  private Vec2f destPos;
  private InterpolationStyle activeStyle;
  private float duration;
  
  private CameraMgr() {
	cameraPos = Vec2f.ZERO;
	activeStyle = InterpolationStyle.NONE;
  }
  
  public static CameraMgr get() {
	  if (singleton == null) {
		  singleton = new CameraMgr();
	  }
	  return singleton;
  }
  
  public void interpolateTo(Vec2f dest, float duration, InterpolationStyle style) {
	  startPos = cameraPos;
	  elapsed = 0.0f;
	  this.duration = duration;
	  destPos = dest;
	  activeStyle = style;
  }
  
  public void setPos(Vec2f pos) {
	  cameraPos = pos;
	  activeStyle = InterpolationStyle.NONE;
  }
  
  public Vec2f getPos() {
	  return cameraPos;
  }
  
  public void update(float seconds) {
	  if (activeStyle == InterpolationStyle.NONE) {
		  return;
	  }
	  elapsed += seconds;
	  if (elapsed > duration) {
		  cameraPos = destPos;
		  activeStyle = InterpolationStyle.NONE;
		  return;
	  }
	  
	  Interpolator i = getInterpolator(activeStyle);
	  assert(i!=null);
	  
	  float newX = i.eval(elapsed, startPos.x, destPos.x-startPos.x, duration);
	  float newY = i.eval(elapsed, startPos.y, destPos.y-startPos.y, duration);

	  cameraPos = new Vec2f(newX, newY);
  }
  
  private Interpolator getInterpolator(InterpolationStyle style) {
	  switch (style) {
	  case LINEAR: return new LinearInterpolator();
	  case EASE_IN_QUAD: return new EaseInInterpolator();
	  case EASE_OUT_QUAD: return new EaseOutInterpolator();
	  }
	  return null;
  }

}

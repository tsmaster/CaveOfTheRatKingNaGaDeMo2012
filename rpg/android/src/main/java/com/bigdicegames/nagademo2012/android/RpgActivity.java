package com.bigdicegames.nagademo2012.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.bigdicegames.nagademo2012.core.Rpg;

public class RpgActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("com/bigdicegames/nagademo2012/resources");
    PlayN.run(new Rpg());
  }
}

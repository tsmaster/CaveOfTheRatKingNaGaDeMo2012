package com.bigdicegames.nagademo2012.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.bigdicegames.nagademo2012.core.Rpg;

public class RpgJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/bigdicegames/nagademo2012/resources");
    platform.setTitle("The Cave of the Rat King");
    PlayN.run(new Rpg());
  }
}

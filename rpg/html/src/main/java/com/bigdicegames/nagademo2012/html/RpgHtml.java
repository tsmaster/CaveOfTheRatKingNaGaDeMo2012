package com.bigdicegames.nagademo2012.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.bigdicegames.nagademo2012.core.Rpg;

public class RpgHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("rpg/");
    platform.setTitle("The Cave of the Rat King");
    PlayN.run(new Rpg());
  }
}

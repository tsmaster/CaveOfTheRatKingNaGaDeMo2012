package com.bigdicegames.nagademo2012.core.modes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse.ButtonEvent;
import playn.core.PlayN;
import playn.core.Pointer.Event;
import playn.core.ResourceCallback;

public class SplashScreenMode implements GameMode {
	
	private String filename;
	private Image image;
	private ImageLayer imageLayer;
	private ImageLayer blankLayer;

	public SplashScreenMode(String filename) {
		this.filename = filename;
	}
	
	@Override
	public void onPopped() {
		graphics().rootLayer().remove(imageLayer);
		graphics().rootLayer().remove(blankLayer);
		imageLayer.destroy();
	}

	@Override
	public void onPushed() {
		image = assets().getImage(filename);
		imageLayer = graphics().createImageLayer(image);
		CanvasImage blankImage = graphics().createImage(graphics().width(), graphics().height());
		blankLayer = graphics().createImageLayer(blankImage);
		Canvas c = blankImage.canvas();
		c.clear();
		c.setFillColor(0xff000000);
		c.fillRect(0, 0, graphics().width(), graphics().height());
		graphics().rootLayer().add(blankLayer);
		
		image.addCallback(new ResourceCallback<Image>(){
			@Override
			public void done(Image resource) {
				float imgWidth = resource.width();
				float imgHeight = resource.height();
				float widthFrac = graphics().width() / imgWidth;
				float heightFrac = graphics().height() / imgHeight;
				float scaleFrac = Math.min(widthFrac, heightFrac);
				imageLayer.setScale(scaleFrac);
				float tx = (graphics().width()-imageLayer.scaledWidth()) / 2.0f;
				float ty = (graphics().height()-imageLayer.scaledHeight()) / 2.0f;
				imageLayer.setTranslation(tx, ty); 
				graphics().rootLayer().add(imageLayer);
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("ImCB: failed to load "+filename);
				PlayN.log().error(err.toString());
			}
		});
	}

	@Override
	public void onUpdate(float deltaSeconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPaint(float alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseDown(ButtonEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPointerStart(Event event) {
		ModeMgr.get().popMode(this);
	}

	@Override
	public void onBecomeTop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBecomeNotTop() {
		// TODO Auto-generated method stub

	}

}

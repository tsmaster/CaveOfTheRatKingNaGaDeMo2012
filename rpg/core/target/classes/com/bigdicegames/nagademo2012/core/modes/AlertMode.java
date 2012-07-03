package com.bigdicegames.nagademo2012.core.modes;

import static playn.core.PlayN.graphics;

import com.bigdicegames.nagademo2012.core.ui.PushButton;
import com.bigdicegames.nagademo2012.core.ui.PushButton.ButtonCallback;
import com.bigdicegames.nagademo2012.core.util.BoundingBox4i;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Mouse.ButtonEvent;
import playn.core.Pointer.Event;
import playn.core.TextFormat.Alignment;
import playn.core.TextFormat;
import playn.core.TextLayout;

public class AlertMode implements GameMode {
	private static final int BUTTON_WIDTH = 150;
	private static final int BUTTON_HEIGHT = 100;
	PushButton okButton;
	private CanvasImage bgImage;
	private ImageLayer bgImageLayer;
	private GroupLayer groupLayer;
	private int backgroundColor;
	private String alertText;
	private int bgWidth;
	private int bgHeight;
	
	public AlertMode() {
		//bgWidth = Math.min(graphics().width(), 1000);
		//bgHeight = Math.min(graphics().height(), 600);
		bgWidth = graphics().width();
		bgHeight = graphics().height();

		bgImage = graphics().createImage(bgWidth, bgHeight);
		bgImageLayer = graphics().createImageLayer(bgImage);
		int bgX = (graphics().width() - bgWidth) / 2;
		int bgY = (graphics().height() - bgHeight) / 2;
		bgImageLayer.setTranslation(bgX, bgY);
		
		int left = (graphics().width() - BUTTON_WIDTH)/2;
		int top = bgY + bgHeight - BUTTON_HEIGHT;
		int right = left + BUTTON_WIDTH;
		int bottom = top + BUTTON_HEIGHT;
		
		okButton = new PushButton("OK", new BoundingBox4i(left, top, right, bottom), new ButtonCallback(){
			@Override
			public void onClicked() {
				ModeMgr.get().popMode(AlertMode.this);
			}});
		
		groupLayer = graphics().createGroupLayer();
		groupLayer.add(bgImageLayer);
		groupLayer.add(okButton.getLayer());
	}
	
	public AlertMode setBackgroundColor(int color) {
		this.backgroundColor = color;
		return this;
	}
	
	public AlertMode setAlertText(String text) {
		this.alertText = text;
		return this;
	}
	
	public void render() {
		Canvas c = bgImage.canvas();
		c.clear();
		c.setFillColor(backgroundColor);
		c.fillRect(0, 0, bgWidth, bgHeight);
		c.setFillColor(0xffffffff);
		TextFormat format = new TextFormat().withAlignment(Alignment.CENTER).withWrapWidth(bgWidth*.75f);
		TextLayout layout = graphics().layoutText(alertText, format);
		c.fillText(layout, (bgWidth-layout.width())/2.0f, (bgHeight-layout.height())/2.0f);
	}
	

	@Override
	public void onPopped() {
		graphics().rootLayer().remove(groupLayer);
	}

	@Override
	public void onPushed() {
		graphics().rootLayer().add(groupLayer);
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
		okButton.tryClick(event);
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
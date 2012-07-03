package com.bigdicegames.nagademo2012.core.ui;


import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Events.Position;
import playn.core.ImageLayer;
import playn.core.TextFormat;
import playn.core.TextFormat.Alignment;
import playn.core.TextLayout;

import com.bigdicegames.nagademo2012.core.util.BoundingBox4i;

public class PushButton {
	public interface ButtonCallback {
		void onClicked();
	}

	private BoundingBox4i boundingBox;
	CanvasImage image;
	ImageLayer imageLayer;
	private ButtonCallback callback;
	private int fillColor;
	private String label;
	private int id;
	
	public PushButton(String label, BoundingBox4i box, ButtonCallback callback){
		this.boundingBox = box;
		image = graphics().createImage(box.width(), box.height());
		imageLayer = graphics().createImageLayer(image);
		imageLayer.setTranslation(box.left, box.top);
		this.fillColor = 0x80808080;
		this.callback = callback;
		setText(label);
	}
	
	public void setText(String label) {
		this.label = label;
		render();
	}
	
	public PushButton setId(int id) {
		this.id = id;
		return this;
	}
	
	public int getId() {
		return id;
	}
	
	private void render() {
		Canvas canvas = image.canvas();
		canvas.clear();
		canvas.setFillColor(fillColor);
		canvas.fillRect(0, 0, boundingBox.width(), boundingBox.height());
		
		canvas.setFillColor(0xffffffff);
		TextFormat format = new TextFormat().withAlignment(Alignment.CENTER).withWrapWidth(boundingBox.width());
		TextLayout layout = graphics().layoutText(label, format);
		canvas.fillText(layout, (boundingBox.width()-layout.width())/2.0f, (boundingBox.height()-layout.height())/2.0f);
	}
	
	public boolean tryClick(Position e) {
		return tryClick(e.x(), e.y());
	}

	private boolean tryClick(float x, float y) {
	    //log().info("try click: "+x+","+y);
		
		if (x >= boundingBox.left &&
			x <= boundingBox.right &&
			y >= boundingBox.top &&
			y <= boundingBox.bottom) {
			callback.onClicked();
			return true;
		}
		return false;
	}
	

	public ImageLayer getLayer() {
		return imageLayer;
	}

	public void setColor(int color) {
		this.fillColor = color;
		render();
	}
}

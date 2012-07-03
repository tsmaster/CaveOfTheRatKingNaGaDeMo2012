package com.bigdicegames.nagademo2012.core.ui;


import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.TextFormat;
import playn.core.TextFormat.Alignment;
import playn.core.TextLayout;

import com.bigdicegames.nagademo2012.core.util.BoundingBox4i;

public class Panel implements Widget {
	public interface ButtonCallback {
		void onClicked();
	}

	private BoundingBox4i boundingBox;
	CanvasImage image;
	ImageLayer imageLayer;
	private int backgroundColor;
	private String label;
	private int foregroundColor;
	
	public Panel(String label, BoundingBox4i box){
		this.boundingBox = box;
		image = graphics().createImage(box.width(), box.height());
		imageLayer = graphics().createImageLayer(image);
		imageLayer.setTranslation(box.left, box.top);
		this.backgroundColor = 0x80404040;
		this.foregroundColor = 0xffffffff;
		setText(label);
	}
	
	public Panel setText(String label) {
		this.label = label;
		render();
		return this;
	}
	
	private void render() {
		Canvas canvas = image.canvas();
		canvas.clear();
		canvas.setFillColor(backgroundColor);
		canvas.fillRect(0, 0, boundingBox.width(), boundingBox.height());
		
		canvas.setFillColor(foregroundColor);
		TextFormat format = new TextFormat().withAlignment(Alignment.CENTER).withWrapWidth(boundingBox.width());
		TextLayout layout = graphics().layoutText(label, format);
		canvas.fillText(layout, (boundingBox.width()-layout.width())/2.0f, (boundingBox.height()-layout.height())/2.0f);
	}

	public ImageLayer getLayer() {
		return imageLayer;
	}

	@Override
	public Panel setBackgroundColor(int color) {
		this.backgroundColor = color;
		render();
		return this;
	}

	@Override
	public Widget setForegroundColor(int color) {
		this.foregroundColor = color;
		render();
		return this;
	}
}

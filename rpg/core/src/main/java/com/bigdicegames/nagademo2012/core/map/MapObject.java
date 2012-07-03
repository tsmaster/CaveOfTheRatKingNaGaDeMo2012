package com.bigdicegames.nagademo2012.core.map;

import static playn.core.PlayN.*;

import com.bigdicegames.nagademo2012.core.math.Vec2f;
import com.bigdicegames.nagademo2012.core.math.Vec2i;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public class MapObject {
	
	private Image image;
	private ImageLayer imageLayer;
	private int tileX;
	private int tileY;

	public MapObject(String filename) {
		log().info("making map object "+filename);
		setIcon(filename);
	}
	
	protected void setIcon(final String filename) {
		if (filename != null && filename.length()>0) {
			image = assets().getImage(filename);
			imageLayer = graphics().createImageLayer(image);
			image.addCallback(new ResourceCallback<Image>(){
				@Override
				public void done(Image resource) {
					centerOnHexPosition();
				}
	
				@Override
				public void error(Throwable err) {
					PlayN.log().error("ImCB: failed to load "+filename);
					PlayN.log().error(err.toString());
				}
			});
		}
	}
	
	public ImageLayer getLayer() {
		return imageLayer;
	}
	
	public void setPosition(int x, int y) {
		tileX = x;
		tileY = y;
		centerOnHexPosition();
	}
	
	public void setPosition(Vec2i tilePos) {
		setPosition(tilePos.x, tilePos.y);
	}
	
	public Vec2i getPosition() {
		return new Vec2i(tileX, tileY);
	}
	
	public void centerOnHexPosition() {
		Vec2f worldPos = Location.getWorldPos(tileX, tileY);
		centerOnWorldPosition(worldPos.x, worldPos.y);
	}

	public void centerOnWorldPosition(float x, float y) {
		if (imageLayer != null) {
			imageLayer.setTranslation(x - image.width() / 2.0f, y - image.height() / 2.0f);
		}
	}

	public void landOn(MapObject obj) {
		obj.isLandedOn(this);
	}

	protected void isLandedOn(MapObject mapObject) {
	}

	public boolean stopsMovement() {
		// TODO Auto-generated method stub
		return false;
	}
}

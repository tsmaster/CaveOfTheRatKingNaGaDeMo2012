package com.bigdicegames.nagademo2012.core.map;

import static playn.core.PlayN.*;

import com.bigdicegames.nagademo2012.core.util.BoundingBox4f;

import playn.core.Image;
import playn.core.ImageLayer;

public class Tile {
	public enum TileType {
		GRASS,
		ROAD,
		SAND,
		SNOW,
		WATER,
		CITY,
		MOUNTAIN,
		GUY,
		HEALER,
		SHOP,
	}

	public static final float TILE_WIDTH = 115.0f;
	public static final float TILE_HEIGHT = 100.0f;
	
	private Image image;
	private ImageLayer imageLayer;
	private int tileX, tileY;
	private BoundingBox4f screenBoundingBox, worldBoundingBox;
	private TileType type;
	private float screenX;
	private float screenY;
	
	public Tile(TileType type, int x, int y) {
		this.type = type; 
		
		this.tileX = x;
		this.tileY = y;
		
		makeWorldBoundingBox();
		
		setCameraPosition(0.0f, 0.0f);
	}
	
	private static String getFilename(TileType type) {
		switch (type) {
		case GRASS: return "images/grasshex_100.png";
		case ROAD: return "images/roadhex_100.png";
		case SAND: return "images/sandhex_100.png";
		case SNOW: return "images/snowhex_100.png";
		case WATER: return "images/waterhex_100.png";
		case CITY: return "images/cityhex_100.png";
		case MOUNTAIN: return "images/mountainhex_100.png";
		case GUY: return "images/guyhex_100.png";
		case HEALER: return "images/healerhex_100.png";
		case SHOP: return "images/shophex_100.png";
		
		default: return null;
		}
	}
	public TileType getType() {
		return type;
	}
	public ImageLayer getLayer() {
		return imageLayer;
	}
	
	private float getWorldX() {
		return 115 * .75f * tileX;
	}
	
	private float getWorldY() {
		float bump= 0.0f;
		if (tileX % 2 != 0) {
			bump = 50.0f;
		}
		return 100.0f * tileY + bump;
	}

	private void makeWorldBoundingBox() {
		float wx = getWorldX();
		float wy = getWorldY();
		this.worldBoundingBox = new BoundingBox4f(wx, wy, wx+115, wy+100);
	}

	public void setCameraPosition(float x, float y) {
		screenX = getWorldX()-x;
		screenY = getWorldY()-y;
		if (imageLayer != null) {
			this.imageLayer.setTranslation(screenX, screenY);
		}
		this.screenBoundingBox = new BoundingBox4f(screenX, screenY, screenX+115, screenY+100);
	}
	
	public BoundingBox4f getScreenBoundingBox() {
		return screenBoundingBox;
	}

	public BoundingBox4f getWorldBoundingBox() {
		return worldBoundingBox;
	}

	public boolean isWalkable() {
		return type != TileType.WATER;
	}

	public boolean isLoaded() {
		return (!(imageLayer == null || imageLayer.destroyed()));
	}
	
	public void unload() {
		if (imageLayer != null) {
			imageLayer.destroy();
		}
	}
	
	public void load() {
		image = assets().getImage(getFilename(type));
		imageLayer = graphics().createImageLayer(image);
		imageLayer.setTranslation(screenX, screenY);
	}
}

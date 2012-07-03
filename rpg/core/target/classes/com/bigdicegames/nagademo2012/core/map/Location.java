package com.bigdicegames.nagademo2012.core.map;

import java.util.ArrayList;

import com.bigdicegames.nagademo2012.core.math.Vec2f;
import com.bigdicegames.nagademo2012.core.math.Vec2i;

import playn.core.PlayN;

/** A location is a space on the map. It will probably have a tile, which is
 * the art for the terrain. It may have an overlay (some additional terrain
 * feature that, well, overlays the terrain). On top of this, there may be one
 * or more MapObjects.  
 *
 * @author Dave LeCompte
 *
 */
public class Location {
	Tile tile;
	Tile overlay;
	ArrayList<MapObject> mapObjects;
	int tileX, tileY;
	private GameMap parentMap;
	
	public Location(Tile tile, int x, int y, GameMap parentMap) {
		this.tile = tile;
		tileX = x;
		tileY = y;
		mapObjects = new ArrayList<MapObject>(1);
		this.parentMap = parentMap;
	}
	
	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public void addMapObject(MapObject object) {
		if (mapObjects.contains(object)) {
			PlayN.log().error("re-adding object " + object + " to location at " + getTileLocationString());
		}
		mapObjects.add(object);
	}
	
	public void removeMapObject(MapObject object) {
		if (!mapObjects.contains(object)) {
			PlayN.log().error("removing object "+object+" that isn't at location " + getTileLocationString());
		}
		mapObjects.remove(object);
	}
	
	public String getTileLocationString() {
		return "(" + tileX + ", " + tileY + ")";
	}

	public boolean containsMapObject(MapObject object) {
		return mapObjects.contains(object);
	}

	public Vec2f getWorldPos() {
		return getWorldPos(tileX, tileY);
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}
	
	public static Vec2f getWorldPos(int tileX, int tileY) {
		float worldX = 115 * .75f * tileX;
		float worldY = 100.0f * tileY;
		if (tileX % 2 != 0) {
			worldY += 50;
		}
		worldX += 115.0f * 0.5f;
		worldY += 50.0f;
		return new Vec2f(worldX, worldY);
	}

	public ArrayList<MapObject> getMapObjects() {
		return mapObjects;
	}
	
	public void clearMapObjects() {
		mapObjects.clear();
	}

	public ArrayList<Location> getNeighbors() {
		return parentMap.getLocationNeighbors(tileX, tileY);
	}

	public Tile getTile() {
		return tile;
	}

	public Vec2i getTilePos() {
		return new Vec2i(tileX, tileY);
	}

}

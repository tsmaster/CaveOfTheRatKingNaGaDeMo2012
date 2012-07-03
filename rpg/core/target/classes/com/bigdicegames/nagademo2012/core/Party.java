package com.bigdicegames.nagademo2012.core;

import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.bigdicegames.nagademo2012.core.InventoryObject.InvType;
import com.bigdicegames.nagademo2012.core.InventoryObject.InventoryProto;
import com.bigdicegames.nagademo2012.core.characters.BaseCharacter;

public class Party {
	
	private static Party singleton;
	private ArrayList<BaseCharacter> characters;
	private int gold;
	private ArrayList<InventoryObject> inventory;
	private MapLocator worldPosn;
	// TODO - update respawnPosn when visiting towns
	private MapLocator respawnPosn;

	private Party() {
		characters = new ArrayList<BaseCharacter>();
		inventory = new ArrayList<InventoryObject>();
	}

	public static Party get() {
		if (singleton == null) {
			singleton = new Party();
		}
		return singleton;
	}
	
	public ArrayList<BaseCharacter> getCharacters() {
		return characters;
	}
	
	public void addCharacter(BaseCharacter record) {
		characters.add(record);
	}
	
	public void addExperience(int experience) {
		int aliveCount = 0;
		for (BaseCharacter c : characters) {
			if (c.isAlive()) {
				aliveCount += 1;
			}
		}
		if (aliveCount == 0) {
			return;
		}
		
		int perCharacter = experience / aliveCount;
		for (BaseCharacter c : characters) {
			if (c.isAlive()) {
				c.addExperiencePoints(perCharacter);
			}
			log().info(c.getName()+" level:"+c.getLevel()+" xp:"+c.getExperiencePoints()+" hp:"+c.getCurrentHitPoints()+"/"+c.getMaxHitPoints());
		}
	}

	public void addGold(int gold) {
		this.gold += gold;
	}
	
	public int getGold() {
		return gold;
	}

	public void respawn() {
		gold = 0;
		for (BaseCharacter c : characters) {
			c.resetHitPoints();
		}
	}

	public void logStatus() {
		log().info("status");
		for (BaseCharacter c : characters) {
			log().info(c.getName());
			log().info(c.getClassName());
			log().info("hp: "+c.getCurrentHitPoints()+"/"+c.getMaxHitPoints());
			log().info("xp: "+c.getExperiencePoints());
			log().info("lv: "+c.getLevel());
		}
	}

	public void addInventory(ArrayList<InventoryObject> drops) {
		inventory.addAll(drops);
	}

	public ArrayList<InventoryObject> getInventory() {
		return inventory;
	}

	public void addInventory(InventoryProto p) {
		inventory.add(new InventoryObject(p));
	}

	public void dropInventory(InventoryObject o) {
		inventory.remove(o);
	}

	public ArrayList<InventoryObject.InventoryProto> getInventoryProtos() {
		ArrayList<InventoryObject.InventoryProto> protoSet = new ArrayList<InventoryObject.InventoryProto>();
		
		for (InventoryObject obj : inventory) {
			InventoryProto proto = obj.getProto();
			if (!protoSet.contains(proto)) {
				protoSet.add(proto);
			}
		}
		Collections.sort(protoSet);
		return protoSet;
	}

	public ArrayList<InventoryProto> getWeaponProtos() {
		ArrayList<InventoryProto> inventoryProtos = getInventoryProtos();
		Iterator<InventoryProto> iterator = inventoryProtos.iterator();
		while (iterator.hasNext()) {
			InventoryProto proto = iterator.next();
			if (proto.type != InvType.WEAPON){
				iterator.remove();
			}
		}
		return inventoryProtos;
	}
	
	public ArrayList<InventoryProto> getArmorProtos() {
		ArrayList<InventoryProto> inventoryProtos = getInventoryProtos();
		Iterator<InventoryProto> iterator = inventoryProtos.iterator();
		while (iterator.hasNext()) {
			InventoryProto proto = iterator.next();
			if (proto.type != InvType.ARMOR){
				iterator.remove();
			}
		}
		return inventoryProtos;
	}

	public void removeProtoFromInventory(InventoryProto proto) {
		Iterator<InventoryObject> iterator = inventory.iterator();
		while (iterator.hasNext()) {
			InventoryObject obj = iterator.next();
			if (obj.getProto() == proto){
				iterator.remove();
				return;
			}
		}
	}

	public void addProtoToInventory(InventoryProto proto) {
		inventory.add(new InventoryObject(proto));
	}
	
}

package com.bigdicegames.nagademo2012.core;



public class InventoryObject {
	/*
	public enum Armor {
		CLOTH(0),
		LEATHER(2),
		STUDDED(3),
		CHAIN(5),
		PLATE(8);
		
		public int armorClass;
		Armor(int armorClass) {
			this.armorClass = armorClass;
		}
	};
	
	public enum Weapon {
		DAGGER(4, 1, 1, 1, 0),
		MACE(6, 1, 1, 1, 0),
		SWORD(8, 1, 0, 1, 0),
		AXE(10, 1, 0, 0, 0),
		BOW(6, 1, 0, 1, 1),
		STAFF(8, 1, 1, 1, 0);
		
		public int damageDie;
		public int useFighter;
		public int useMage;
		public int useRanger;
		public int canFire;

		Weapon(int damage, int f, int m, int r, int fire) {
			damageDie = damage;
			useFighter = f;
			useMage = m;
			useRanger = r;
			canFire = fire;
		}
	}*/
	
	
	
	public enum InvType {
		WEAPON,
		ARMOR,
		EQUIPMENT
	}
	
	public enum InventoryProto {
		INV_DAGGER ("dagger", 1, 2, InvType.WEAPON, 4),
		INV_AXE ("axe", 2, 4, InvType.WEAPON, 6),
		INV_MACE ("mace", 2, 4, InvType.WEAPON, 6),
		INV_SWORD ("sword", 4, 8, InvType.WEAPON, 8),
		INV_BOW ("bow", 4, 8, InvType.WEAPON, 6),
		INV_STAFF ("staff", 4, 8, InvType.WEAPON, 6),
		INV_CLOTH_ARMOR ("cloth armor", 2, 4, InvType.ARMOR, 2),
		INV_LEATHER_ARMOR ("leather armor", 3, 6, InvType.ARMOR, 4),
		INV_STUDDED_ARMOR ("studded armor", 3, 6, InvType.ARMOR, 6),
		INV_CHAIN_MAIL ("chain mail", 5, 10, InvType.ARMOR, 8),
		INV_PLATE_MAIL ("plate mail", 25, 50, InvType.ARMOR, 10);
		
		public String name;
		public int sell;
		public int buy;
		public InvType type;
		public int armorClass;
		public int damageDie;

		InventoryProto(String name, int sell, int buy, InvType type, int val) {
			this.name = name;
			this.sell = sell;
			this.buy = buy;
			this.type = type;
			if (type == InvType.WEAPON) {
				damageDie = val;
			} else if (type == InvType.ARMOR) {
				armorClass = val;
			}
		}
	}
	
	private InventoryProto proto;

	public String getName() {
		return proto.name;
	}
	
	public InventoryObject(InventoryProto proto) {
		this.proto = proto;
	}

	public int getBuyPrice() {
		return proto.buy;
	}

	public int getSellPrice() {
		return proto.sell;
	}

	public InventoryProto getProto() {
		return proto;
	}
}
package com.bigdicegames.nagademo2012.core.modes;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.Collections;

import com.bigdicegames.nagademo2012.core.InventoryObject.InventoryProto;
import com.bigdicegames.nagademo2012.core.Party;
import com.bigdicegames.nagademo2012.core.characters.BaseCharacter;
import com.bigdicegames.nagademo2012.core.ui.Panel;
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

public class GearMode implements GameMode {
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
	private int characterIndex;
	private PushButton weaponLeftButton;
	private PushButton weaponRightButton;
	private Panel weaponPanel;
	private PushButton armorLeftButton;
	private PushButton armorRightButton;
	private Panel armorPanel;
	
	public GearMode(int i) {
		characterIndex = i;
		bgWidth = Math.min(graphics().width(), 1000);
		bgHeight = Math.min(graphics().height(), 600);

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
				ModeMgr.get().popMode(GearMode.this);
			}});
		
		top = bgY + 100;
		
		weaponLeftButton = new PushButton("<<W", new BoundingBox4i(bgX, top, bgX + 100, top + 100), new ButtonCallback(){
			@Override
			public void onClicked() {
				weaponRotate(-1);
			}});
		
		weaponRightButton = new PushButton("W>>", new BoundingBox4i(bgX+bgWidth-100, top, bgX+bgWidth, top + 100), new ButtonCallback(){
			@Override
			public void onClicked() {
				weaponRotate(1);
			}});
		
		weaponPanel = new Panel("weapon", new BoundingBox4i(bgX+100, top, bgX+bgWidth-100, top+100));

		top  = bgY + 250;
		
		armorLeftButton = new PushButton("<<A", new BoundingBox4i(bgX, top, bgX+100, top + 100), new ButtonCallback(){
			@Override
			public void onClicked() {
				armorRotate(-1);
			}});
		
		armorRightButton = new PushButton("A>>", new BoundingBox4i(bgX+bgWidth-100, top, bgX+bgWidth, top + 100), new ButtonCallback(){
			@Override
			public void onClicked() {
				armorRotate(1);
			}});
		
		armorPanel = new Panel("armor", new BoundingBox4i(bgX+100, top, bgX+bgWidth-100, top+100));

		
		groupLayer = graphics().createGroupLayer();
		groupLayer.add(bgImageLayer);
		groupLayer.add(okButton.getLayer());
		
		groupLayer.add(weaponLeftButton.getLayer());
		groupLayer.add(weaponRightButton.getLayer());
		groupLayer.add(weaponPanel.getLayer());
		
		groupLayer.add(armorLeftButton.getLayer());
		groupLayer.add(armorRightButton.getLayer());
		groupLayer.add(armorPanel.getLayer());
		
		setBackgroundColor(0xff804080).setAlertText("Select Equipment for "+Party.get().getCharacters().get(characterIndex).getName()).render();
		updateUI();
	}
	
	private void updateUI() {
		BaseCharacter c = Party.get().getCharacters().get(characterIndex);
		InventoryProto p = c.getWeaponProto();
		if (p != null) {
			weaponPanel.setText("weapon:" + p.name);
		} else {
			weaponPanel.setText("weapon: none");
		}
		p = c.getArmorProto();
		if (p != null) {
			armorPanel.setText("armor:" + p.name);
		} else {
			armorPanel.setText("armod: none");
		}
	}

	protected void weaponRotate(int i) {
		ArrayList<InventoryProto> inventoryProtos = Party.get().getWeaponProtos();
		BaseCharacter character = Party.get().getCharacters().get(characterIndex);
		InventoryProto currentProto = character.getWeaponProto();
		if (currentProto != null) {
			int currentIndex = inventoryProtos.indexOf(currentProto);
			if (currentIndex == -1) {
				inventoryProtos.add(currentProto);
				Collections.sort(inventoryProtos);
				currentIndex = inventoryProtos.indexOf(currentProto);
			}
			if (inventoryProtos.size() > 1) {
				int nextIndex = (currentIndex + i) % inventoryProtos.size();
				InventoryProto newProto = inventoryProtos.get(nextIndex);
				Party.get().removeProtoFromInventory(newProto);
				Party.get().addProtoToInventory(currentProto);
				character.setWeaponProto(newProto);
			}
		} else {
			if (inventoryProtos.size() > 0) {
				InventoryProto newProto;
				if (i > 0) {
					newProto = inventoryProtos.get(0);
				} else {
					newProto = inventoryProtos.get(inventoryProtos.size() - 1);
				}
				Party.get().removeProtoFromInventory(newProto);
				character.setWeaponProto(newProto);
			}
		}
		updateUI();
	}
	
	protected void armorRotate(int i) {
		ArrayList<InventoryProto> inventoryProtos = Party.get().getArmorProtos();
		BaseCharacter character = Party.get().getCharacters().get(characterIndex);
		InventoryProto currentProto = character.getArmorProto();
		if (currentProto != null) {
			int currentIndex = inventoryProtos.indexOf(currentProto);
			if (currentIndex == -1) {
				inventoryProtos.add(currentProto);
				Collections.sort(inventoryProtos);
				currentIndex = inventoryProtos.indexOf(currentProto);
			}
			if (inventoryProtos.size() > 1) {
				int nextIndex = (currentIndex + i) % inventoryProtos.size();
				InventoryProto newProto = inventoryProtos.get(nextIndex);
				Party.get().removeProtoFromInventory(newProto);
				Party.get().addProtoToInventory(currentProto);
				character.setArmorProto(newProto);
			}
		} else {
			if (inventoryProtos.size() > 0) {
				InventoryProto newProto;
				if (i > 0) {
					newProto = inventoryProtos.get(0);
				} else {
					newProto = inventoryProtos.get(inventoryProtos.size() - 1);
				}
				Party.get().removeProtoFromInventory(newProto);
				character.setArmorProto(newProto);
			}
		}
		updateUI();
	}

	public GearMode setBackgroundColor(int color) {
		this.backgroundColor = color;
		return this;
	}
	
	public GearMode setAlertText(String text) {
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
		c.fillText(layout, (bgWidth-layout.width())/2.0f, layout.height());
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
		if (okButton.tryClick(event)) {
			return;
		}
		if (weaponLeftButton.tryClick(event)) {
			return;
		}
		if (weaponRightButton.tryClick(event)) {
			return;
		}
		if (armorLeftButton.tryClick(event)) {
			return;
		}
		if (armorRightButton.tryClick(event)) {
			return;
		}
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

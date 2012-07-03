package com.bigdicegames.nagademo2012.core.modes;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;

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
import playn.core.TextFormat;
import playn.core.TextLayout;
import playn.core.Mouse.ButtonEvent;
import playn.core.Pointer.Event;
import playn.core.TextFormat.Alignment;

public class HealerMode implements GameMode {

	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 80;
	private int bgWidth;
	private int bgHeight;
	private ImageLayer bgImageLayer;
	private PushButton okButton;
	private GroupLayer groupLayer;
	private int backgroundColor;
	private CanvasImage bgImage;
	private String alertText;
	private ArrayList<PushButton> healButtons;
	private Panel goldPanel;
	
	public HealerMode() {
		healButtons = new ArrayList<PushButton>();
	}

	@Override
	public void onPopped() {
		graphics().rootLayer().remove(groupLayer);
		bgImageLayer.destroy();
		okButton = null;
		groupLayer.destroy();
	}

	@Override
	public void onPushed() {
		bgWidth = Math.min(graphics().width(), 1000);
		bgHeight = Math.min(graphics().height(), 600);

		bgImage = graphics().createImage(bgWidth, bgHeight);
		bgImageLayer = graphics().createImageLayer(bgImage);
		int bgX = (graphics().width() - bgWidth) / 2;
		int bgY = (graphics().height() - bgHeight) / 2;
		bgImageLayer.setTranslation(bgX, bgY);
		
		groupLayer = graphics().createGroupLayer();
		groupLayer.add(bgImageLayer);
		
		ArrayList<BaseCharacter> characters = Party.get().getCharacters();
		for (int i = 0; i< characters.size(); ++i) {
			final BaseCharacter c = characters.get(i);
			int left = (graphics().width() - BUTTON_WIDTH)/4 + graphics().width() * i / 4;
			int top = bgY + BUTTON_HEIGHT;
			int right = left + BUTTON_WIDTH;
			int bottom = top + BUTTON_HEIGHT;
			
			PushButton healButton = new PushButton("Heal "+ c.getName(), new BoundingBox4i(left, top, right, bottom), new ButtonCallback() {
				@Override
				public void onClicked() {
					tryToHeal(c);
				}
			});
			
			healButtons.add(healButton);
			groupLayer.add(healButton.getLayer());
		}

		int left = (graphics().width() - BUTTON_WIDTH)/2;
		int top = (graphics().height() - BUTTON_HEIGHT)/2;
		int right = left + BUTTON_WIDTH;
		int bottom = top + BUTTON_HEIGHT;
		
		goldPanel = new Panel("Gold", new BoundingBox4i(left, top, right, bottom));
		groupLayer.add(goldPanel.getLayer());
		updateWidgets();
		
		left = (graphics().width() - BUTTON_WIDTH)/2;
		top = bgY + bgHeight - BUTTON_HEIGHT;
		right = left + BUTTON_WIDTH;
		bottom = top + BUTTON_HEIGHT;
		
		okButton = new PushButton("DONE", new BoundingBox4i(left, top, right, bottom), new ButtonCallback(){
			@Override
			public void onClicked() {
				ModeMgr.get().popMode(HealerMode.this);
			}});
		
		groupLayer.add(okButton.getLayer());
		graphics().rootLayer().add(groupLayer);
		this.setBackgroundColor(0xff808040)
			.setAlertText("Who do you want to heal?")
			.render();
	}
	
	private void updateWidgets() {
		int partyGold = Party.get().getGold();
		goldPanel.setText("Party Gold: " + partyGold);
		
		ArrayList<BaseCharacter> characters = Party.get().getCharacters();
		
		for (int i = 0; i< characters.size(); ++i) {
			final BaseCharacter c = characters.get(i);
			int cost = getHealCost(c);
			String label;
			if (cost > partyGold) {
				label = c.getName()+": ["+cost+"]";
			} else if (cost == 0) {
				label = c.getName()+": healthy";
			} else {
				label = c.getName()+": "+cost;
			}
			
			PushButton b = healButtons.get(i);
			b.setText(label);
		}
	}

	protected void tryToHeal(BaseCharacter c) {
		int cost = getHealCost(c);
		if (cost <= Party.get().getGold()) {
			c.resetHitPoints();
			Party.get().addGold(-cost);
			updateWidgets();
		}
	}
	
	protected int getHealCost(BaseCharacter c) {
		if (!c.isAlive()) {
			return c.getLevel() * 100 + 100;
		} else {
			int diffHp = c.getMaxHitPoints() - c.getCurrentHitPoints();
			return diffHp * 10;
		}
	}

	public HealerMode setBackgroundColor(int color) {
		this.backgroundColor = color;
		return this;
	}
	
	public HealerMode setAlertText(String text) {
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
		for (PushButton b : healButtons) {
			if (b.tryClick(event)) {
				return;
			}
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

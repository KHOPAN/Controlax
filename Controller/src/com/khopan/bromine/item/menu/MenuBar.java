package com.khopan.bromine.item.menu;

import java.util.ArrayList;
import java.util.List;

import com.khopan.bromine.Item;
import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.animation.transform.ColorTransform;
import com.khopan.bromine.area.Area;
import com.khopan.bromine.input.Mouse;
import com.khopan.bromine.theme.Theme;

public class MenuBar extends Item<MenuBar> {
	private final List<MenuOption> optionList;
	private final ColorTransform transform;

	private int strokeWeight;
	private int lineColor;
	private int spacing;

	public MenuBar() {
		this.optionList = new ArrayList<>();
		this.transform = new ColorTransform();
		this.transform.duration().set(150);
		this.transform.value().set(this.theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR));
		this.transform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.transform.framerate().set(240);
		this.transform.ticker().set(color -> {
			this.lineColor = color.getRGB();
			this.update();
		});

		this.strokeWeight = 1;
		this.lineColor = this.theme.getRGB(Theme.KEY_HOVERED_ITEM_BORDER_COLOR);
		this.spacing = 2;
	}

	@Override
	protected boolean rootMouseAction(Mouse mouse) {
		for(int i = 0; i < this.optionList.size(); i++) {
			if(this.optionList.get(i).rootMouseAction(mouse)) {
				return true;
			}
		}

		return false;
	}

	public MenuBar addOption(MenuOption option) {
		if(option.menuInstance != null) {
			throw new IllegalArgumentException("Option already have a parent");
		}

		MenuInstance instance = new MenuInstance();
		instance.onUpdate = this :: update;
		instance.onClose = () -> {};
		instance.theme = () -> this.theme;
		option.menuInstance = instance;
		option.initialize();
		this.optionList.add(option);
		return this;
	}

	@Override
	protected void onThemeUpdate(Theme theme) {
		this.transform.begin(theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR));

		for(int i = 0; i < this.optionList.size(); i++) {
			this.optionList.get(i).onThemeUpdate();
		}
	}

	@Override
	protected void onResize() {
		int size = this.bounds.height - this.strokeWeight - this.spacing * 2;
		double x = this.spacing;

		for(int i = 0; i < this.optionList.size(); i++) {
			MenuOption option = this.optionList.get(i);
			option.bounds.x = x;
			option.bounds.y = this.spacing;
			option.bounds.height = size;
			option.calculateWidth();
			option.bounds.width = option.actualWidth;
			option.popupX = (int) Math.round(x);
			option.popupY = this.bounds.height - this.strokeWeight;
			option.onResize();

			if(option.bounds.width > 0.0d) {
				x += option.bounds.width + this.spacing;
			}
		}
	}

	@Override
	protected void render(Area area) {
		for(int i = 0; i < this.optionList.size(); i++) {
			this.optionList.get(i).render(area);
		}

		area.color(this.lineColor);
		area.fill();
		area.rect(0, this.bounds.height - this.strokeWeight, this.bounds.width, this.strokeWeight);
	}

	@Override
	protected void renderTop(Area area) {
		for(int i = 0; i < this.optionList.size(); i++) {
			this.optionList.get(i).renderTop(area);
		}
	}

	private void optionMouseAction(MenuOption option, int action, Mouse mouse) {
		option.mouseAction(new Mouse(mouse.button, mouse.clickCount, action, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, mouse.location, System.currentTimeMillis(), mouse.scrollType, mouse.scrollAmount, mouse.wheelRotation, mouse.preciseWheelRotation));
	}

	@Override
	protected void mouseAction(Mouse mouse) {
		if(mouse.action == Mouse.ACTION_DRAGGED) {
			for(int i = 0; i < this.optionList.size(); i++) {
				MenuOption option = this.optionList.get(i);

				if(option.bounds.contains(mouse.location)) {
					if(option.internalEntered) {
						this.optionMouseAction(option, Mouse.ACTION_DRAGGED, mouse);
					} else {
						option.internalEntered = true;
						this.optionMouseAction(option, Mouse.ACTION_ENTERED, mouse);
					}
				} else {
					if(option.internalEntered) {
						option.internalEntered = false;
						this.optionMouseAction(option, Mouse.ACTION_EXITED, mouse);
					}
				}
			}
		} else if(mouse.action == Mouse.ACTION_MOVED || mouse.action == Mouse.ACTION_ENTERED || mouse.action == Mouse.ACTION_EXITED) {
			for(int i = 0; i < this.optionList.size(); i++) {
				MenuOption option = this.optionList.get(i);

				if(option.bounds.contains(mouse.location)) {
					if(!option.internalEntered) {
						option.internalEntered = true;
						this.optionMouseAction(option, Mouse.ACTION_ENTERED, mouse);
					} else {
						this.optionMouseAction(option, Mouse.ACTION_MOVED, mouse);
					}
				} else {
					if(option.internalEntered) {
						option.internalEntered = false;
						this.optionMouseAction(option, Mouse.ACTION_EXITED, mouse);
					}
				}
			}
		} else if(mouse.action == Mouse.ACTION_PRESSED) {
			for(int i = 0; i < this.optionList.size(); i++) {
				MenuOption option = this.optionList.get(i);

				if(option.bounds.contains(mouse.location) && option.internalEntered) {
					option.internalPressed = true;
					this.optionMouseAction(option, Mouse.ACTION_PRESSED, mouse);
					this.update();
				}
			}
		} else if(mouse.action == Mouse.ACTION_RELEASED) {
			for(int i = 0; i < this.optionList.size(); i++) {
				MenuOption option = this.optionList.get(i);

				if(option.internalPressed) {
					option.internalPressed = false;
					this.optionMouseAction(option, Mouse.ACTION_RELEASED, mouse);
				}

				if(option.bounds.contains(mouse.location) && option.internalEntered) {
					this.optionMouseAction(option, Mouse.ACTION_CLICKED, mouse);
				}
			}
		} else if(mouse.action == Mouse.ACTION_WHEEL_MOVED) {
			for(int i = 0; i < this.optionList.size(); i++) {
				MenuOption option = this.optionList.get(i);

				if(option.bounds.contains(mouse.location) && option.internalEntered) {
					this.optionMouseAction(option, Mouse.ACTION_WHEEL_MOVED, mouse);
				}
			}
		}
	}
}

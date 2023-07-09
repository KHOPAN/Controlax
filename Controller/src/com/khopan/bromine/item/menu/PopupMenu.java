package com.khopan.bromine.item.menu;

import java.awt.AlphaComposite;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.animation.interpolator.value.ColorInterpolator;
import com.khopan.bromine.animation.transform.DoubleTransform;
import com.khopan.bromine.animation.transform.FloatTransform;
import com.khopan.bromine.area.Area;
import com.khopan.bromine.input.Mouse;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;
import com.khopan.bromine.theme.LightTheme;
import com.khopan.bromine.theme.Theme;
import com.khopan.bromine.utils.Geometry;

public class PopupMenu {
	public final RoundRectangle2D.Double bounds;

	final List<MenuOption> optionList;
	Runnable onClose;

	private final FloatTransform transparentTransform;
	private final DoubleTransform themeTransform;
	private final ColorInterpolator backgroundInterpolator;
	private final ColorInterpolator borderInterpolator;

	public Runnable onUpdate;
	public double borderThickness;
	public double spacing;

	private Theme theme;
	private boolean visible;
	private boolean hide;
	private float transparent;
	private Shape background;
	private Shape border;

	public PopupMenu() {
		this.bounds = new RoundRectangle2D.Double();
		this.optionList = new ArrayList<>();
		this.transparentTransform = new FloatTransform();
		this.transparentTransform.duration().set(150);
		this.transparentTransform.value().set(0.0f);
		this.transparentTransform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.transparentTransform.framerate().set(240);
		this.transparentTransform.ticker().set(transparent -> {
			this.transparent = transparent;

			if(this.transparent == 0.0f && this.hide) {
				this.visible = false;
			}

			if(this.onUpdate != null) {
				this.onUpdate.run();
			}
		});

		this.backgroundInterpolator = new ColorInterpolator();
		this.borderInterpolator = new ColorInterpolator();
		this.themeTransform = new DoubleTransform();
		this.themeTransform.duration().set(150);
		this.themeTransform.value().set(0.0d);
		this.themeTransform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.themeTransform.framerate().set(240);
		this.themeTransform.ticker().set(time -> {
			this.backgroundInterpolator.interpolate(time);
			this.borderInterpolator.interpolate(time);

			if(this.onUpdate != null) {
				this.onUpdate.run();
			}
		});

		this.theme = LightTheme.THEME;
		this.backgroundInterpolator.value = this.theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR);
		this.borderInterpolator.value = this.theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR);
		this.spacing = 6.0d;
		this.visible = false;
	}

	public Property<Theme, PopupMenu> theme() {
		return new SimpleProperty<Theme, PopupMenu>(() -> this.theme, theme -> this.theme = theme, theme -> {
			this.backgroundInterpolator.begin(this.theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR));
			this.borderInterpolator.begin(this.theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR));
			this.themeTransform.begin(0.0d, 1.0d);

			for(int i = 0; i < this.optionList.size(); i++) {
				this.optionList.get(i).onThemeUpdate();
			}
		}, this).nullable().whenNull(LightTheme.THEME);
	}

	public PopupMenu addOption(MenuOption option) {
		if(option.menuInstance != null) {
			throw new IllegalArgumentException("Option already have a parent");
		}

		MenuInstance instance = new MenuInstance();
		instance.onUpdate = () -> {
			if(this.onUpdate != null) {
				this.onUpdate.run();
			}
		};

		instance.onClose = () -> {
			if(this.onClose != null) {
				this.onClose.run();
			}

			this.hide();
		};

		instance.theme = () -> this.theme;
		option.menuInstance = instance;
		option.popup = true;
		option.initialize();
		this.optionList.add(option);
		return this;
	}

	public void show(int x, int y) {
		this.bounds.x = x;
		this.bounds.y = y;
		this.hide = false;
		this.onResize();

		if(!this.visible) {
			this.visible = true;
			this.transparentTransform.begin(1.0f);
		} else {
			this.hide();
		}
	}

	public void hide() {
		this.hide = true;
		this.transparentTransform.begin(0.0f);
	}

	public void onResize() {
		if(this.borderThickness < 1.0d) {
			this.borderThickness = 1.0d;
		}

		double optionWidth = 0.0d;

		for(int i = 0; i < this.optionList.size(); i++) {
			MenuOption option = this.optionList.get(i);
			option.calculateWidth();
			optionWidth = Math.max(optionWidth, option.actualWidth);
		}

		double y = this.spacing;

		for(int i = 0; i < this.optionList.size(); i++) {
			MenuOption option = this.optionList.get(i);
			option.bounds.x = this.bounds.x + this.spacing;
			option.bounds.y = this.bounds.y + y;
			option.bounds.width = optionWidth;
			option.bounds.height = 25.0d;
			option.spacing = this.spacing;
			option.onResize();

			if(option.bounds.height > 0.0d) {
				y += option.bounds.height + this.spacing;
			}
		}

		this.bounds.width = optionWidth + this.spacing * 2.0d;
		this.bounds.height = y;
		double arc = 15.0d;
		this.bounds.arcwidth = arc;
		this.bounds.archeight = arc;
		RoundRectangle2D.Double rectangle = new RoundRectangle2D.Double();
		rectangle.x = this.bounds.x + this.borderThickness;
		rectangle.y = this.bounds.y + this.borderThickness;
		double doubleThickness = this.borderThickness * 2.0d;
		rectangle.width = this.bounds.width - doubleThickness;
		rectangle.height = this.bounds.height - doubleThickness;
		rectangle.arcwidth = this.bounds.arcwidth - doubleThickness;
		rectangle.archeight = this.bounds.archeight - doubleThickness;
		this.border = Geometry.subtractShape(this.bounds, rectangle);
		this.background = rectangle;

		for(int i = 0; i < this.optionList.size(); i++) {
			this.background = Geometry.subtractShape(this.background, this.optionList.get(i).bounds);
		}
	}

	public void render(Area area) {
		for(int i = 0; i < this.optionList.size(); i++) {
			this.optionList.get(i).renderTop(area);
		}

		if(this.visible) {
			area = area.create();
			area.smooth();
			area.composite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.transparent));
			area.color(this.backgroundInterpolator.value);
			area.fill(this.background);
			area.color(this.borderInterpolator.value);
			area.fill(this.border);

			for(int i = 0; i < this.optionList.size(); i++) {
				this.optionList.get(i).render(area);
			}
		}
	}

	private void optionMouseAction(MenuOption option, int action, Mouse mouse) {
		option.mouseAction(new Mouse(mouse.button, mouse.clickCount, action, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, mouse.location, System.currentTimeMillis(), mouse.scrollType, mouse.scrollAmount, mouse.wheelRotation, mouse.preciseWheelRotation));
	}

	public boolean mouseAction(Mouse mouse) {
		for(int i = 0; i < this.optionList.size(); i++) {
			if(this.optionList.get(i).rootMouseAction(mouse)) {
				return true;
			}
		}

		if(this.visible) {
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

					if(option.bounds.contains(mouse.location)) {
						if(option.internalEntered) {
							option.internalPressed = true;
							this.optionMouseAction(option, Mouse.ACTION_PRESSED, mouse);
							this.onResize();
						}
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

			if(this.bounds.contains(mouse.location)) {
				return true;
			} else {
				if(mouse.action == Mouse.ACTION_RELEASED) {
					this.hide();
				}

				return false;
			}
		}

		return false;
	}
}

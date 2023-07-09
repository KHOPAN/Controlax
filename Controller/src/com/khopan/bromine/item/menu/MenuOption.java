package com.khopan.bromine.item.menu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Polygon;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;

import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.animation.interpolator.value.ColorInterpolator;
import com.khopan.bromine.animation.transform.DoubleTransform;
import com.khopan.bromine.area.Area;
import com.khopan.bromine.input.Mouse;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;
import com.khopan.bromine.theme.Theme;

public class MenuOption {
	MenuInstance menuInstance;
	boolean internalEntered;
	boolean internalPressed;
	double actualWidth;
	boolean hasArrow;
	int popupX;
	int popupY;
	boolean popup;
	double spacing;
	final RoundRectangle2D.Double bounds;

	private final RoundRectangle2D.Double backgroundRectangle;
	private final RoundRectangle2D.Double arrowRectangle;
	private final RoundRectangle2D.Double arrowBackground;
	private final Polygon arrow;
	private final DoubleTransform transform;
	private final ColorInterpolator foreground;
	private final ColorInterpolator background;
	private final ColorInterpolator border;
	private PopupMenu popupMenu;
	private boolean entered;
	private boolean pressed;
	private double roundness;
	private double borderThickness;

	private String text;
	private Font font;
	private Runnable action;

	public MenuOption() {
		this.bounds = new RoundRectangle2D.Double();
		this.backgroundRectangle = new RoundRectangle2D.Double();
		this.arrowRectangle = new RoundRectangle2D.Double();
		this.arrowBackground = new RoundRectangle2D.Double();
		this.arrow = new Polygon();
		this.foreground = new ColorInterpolator();
		this.background = new ColorInterpolator();
		this.border = new ColorInterpolator();
		this.transform = new DoubleTransform();
		this.transform.duration().set(150);
		this.transform.value().set(0.0d);
		this.transform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.transform.framerate().set(240);
		this.transform.ticker().set(time -> {
			this.foreground.interpolate(time);
			this.background.interpolate(time);
			this.border.interpolate(time);
			this.menuInstance.update();
		});

		this.text = "";
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
	}

	public MenuOption addOption(MenuOption option) {
		if(option == null) {
			throw new IllegalArgumentException("Option cannot be null");
		}

		if(option == this) {
			throw new IllegalArgumentException("Option cannot add itself");
		}

		if(this.popupMenu == null) {
			this.popupMenu = new PopupMenu();
			this.popupMenu.onUpdate = () -> {
				this.menuInstance.update();
			};

			this.popupMenu.onClose = () -> {
				if(this.menuInstance.onClose != null) {
					this.menuInstance.onClose.run();
				}
			};
		}

		this.popupMenu.addOption(option);
		this.onResize();
		return this;
	}

	public Property<String, MenuOption> text() {
		return new SimpleProperty<String, MenuOption>(() -> this.text, text -> this.text = text, text -> {
			this.onResize();

			if(this.menuInstance != null) {
				this.menuInstance.update();
			}
		}, this).nullable().whenNull("");
	}

	public Property<Font, MenuOption> font() {
		return new SimpleProperty<Font, MenuOption>(() -> this.font, font -> this.font = font, font -> {
			this.onResize();

			if(this.menuInstance != null) {
				this.menuInstance.update();
			}
		}, this).nullable().whenNull(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
	}

	public Property<Runnable, MenuOption> action() {
		return new SimpleProperty<Runnable, MenuOption>(() -> this.action, action -> this.action = action, this).nullable();
	}

	void initialize() {
		Theme theme = this.menuInstance.theme.get();
		this.foreground.value = theme.getColor(Theme.KEY_TEXT_COLOR);
		this.background.value = theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR);
		this.border.value = theme.getColor(Theme.KEY_ITEM_BORDER_COLOR);
	}

	void onThemeUpdate() {
		if(this.entered) {
			if(this.pressed) {
				this.pressed();
			} else {
				this.hovered();
			}
		} else {
			this.normal();
		}

		if(this.popupMenu != null && this.menuInstance != null) {
			this.popupMenu.theme().set(this.menuInstance.theme.get());
		}
	}

	private void normal() {
		Theme theme = this.menuInstance.theme.get();
		this.foreground.begin(theme.getColor(Theme.KEY_TEXT_COLOR));
		this.background.begin(theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR));
		this.border.begin(theme.getColor(Theme.KEY_ITEM_BORDER_COLOR));
		this.transform.begin(0.0d, 1.0d);
	}

	private void hovered() {
		Theme theme = this.menuInstance.theme.get();
		this.foreground.begin(theme.getColor(Theme.KEY_HOVERED_TEXT_COLOR));
		this.background.begin(theme.getColor(Theme.KEY_HOVERED_ITEM_BACKGROUND_COLOR));
		this.border.begin(theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR));
		this.transform.begin(0.0d, 1.0d);
	}

	private void pressed() {
		Theme theme = this.menuInstance.theme.get();
		this.foreground.begin(theme.getColor(Theme.KEY_PRESSED_TEXT_COLOR));
		this.background.begin(theme.getColor(Theme.KEY_PRESSED_ITEM_BACKGROUND_COLOR));
		this.border.begin(theme.getColor(Theme.KEY_PRESSED_ITEM_BORDER_COLOR));
		this.transform.begin(0.0d, 1.0d);
	}

	void mouseAction(Mouse mouse) {
		if(mouse.action == Mouse.ACTION_MOVED || mouse.action == Mouse.ACTION_ENTERED || mouse.action == Mouse.ACTION_EXITED) {
			if(this.bounds.contains(mouse.location)) {
				if(!this.entered) {
					this.entered = true;
					this.hovered();
				}
			} else {
				if(this.entered) {
					this.entered = false;
					this.normal();
				}
			}
		} else if(mouse.action == Mouse.ACTION_PRESSED) {
			if(this.entered) {
				this.pressed = true;
				this.pressed();
			}
		} else if(mouse.action == Mouse.ACTION_RELEASED) {
			if(this.entered && this.pressed) {
				this.pressed = false;
				this.runAction();
				this.hovered();
			}
		}
	}

	private void runAction() {
		if(this.popupMenu != null) {
			if(this.popup) {
				this.onResize();
				this.popupX = (int) Math.round(this.bounds.x + this.bounds.width + this.spacing - this.borderThickness);
				this.popupY = (int) Math.round(this.bounds.y);
			}

			this.popupMenu.show(this.popupX, this.popupY);
		} else {
			if(this.action != null) {
				this.action.run();
			}

			this.menuInstance.onClose.run();
		}
	}

	boolean rootMouseAction(Mouse mouse) {
		if(this.popupMenu != null) {
			return this.popupMenu.mouseAction(mouse);
		}

		return false;
	}

	void renderTop(Area area) {
		if(this.popupMenu != null) {
			this.popupMenu.render(area);
		}
	}

	void calculateWidth() {
		FontMetrics metrics = new JLabel().getFontMetrics(this.font);
		this.actualWidth = ((double) metrics.stringWidth(this.text)) + (this.menuInstance != null && !this.text.isEmpty() ? (this.bounds.height - ((double) metrics.getAscent())) : 0.0d) + (this.hasArrow ? this.bounds.height : 0.0d);
	}

	void onResize() {
		if(this.popupMenu != null) {
			this.hasArrow = this.popup && !this.popupMenu.optionList.isEmpty();

			if(this.hasArrow) {
				this.popupX = 0;
				this.popupY = 0;
			}

			this.popupMenu.onResize();
		}

		this.roundness = Math.min(this.bounds.width, this.bounds.height) * 0.5d;
		this.borderThickness = Math.min(this.bounds.width, this.bounds.height) * 0.04d;
		this.bounds.arcwidth = this.roundness;
		this.bounds.archeight = this.roundness;
		this.backgroundRectangle.x = this.bounds.x + this.borderThickness;
		this.backgroundRectangle.y = this.bounds.y + this.borderThickness;
		double doubleThickness = this.borderThickness * 2;
		this.backgroundRectangle.width = this.bounds.width - doubleThickness;
		this.backgroundRectangle.height = this.bounds.height - doubleThickness;
		this.backgroundRectangle.arcwidth = this.bounds.arcwidth - doubleThickness;
		this.backgroundRectangle.archeight = this.bounds.archeight - doubleThickness;

		if(this.hasArrow) {
			this.arrowRectangle.x = this.bounds.x + this.bounds.width - this.bounds.height;
			this.arrowRectangle.y = this.bounds.y;
			this.arrowRectangle.width = this.bounds.height;
			this.arrowRectangle.height = this.bounds.height;
			this.arrowRectangle.arcwidth = this.bounds.arcwidth;
			this.arrowRectangle.archeight = this.bounds.archeight;
			this.arrowBackground.x = this.arrowRectangle.x + this.borderThickness;
			this.arrowBackground.y = this.arrowRectangle.y + this.borderThickness;
			this.arrowBackground.width = this.arrowRectangle.width - doubleThickness;
			this.arrowBackground.height = this.arrowRectangle.height - doubleThickness;
			this.arrowBackground.arcwidth = this.arrowRectangle.arcwidth - doubleThickness;
			this.arrowBackground.archeight = this.arrowRectangle.archeight - doubleThickness;
			this.arrow.npoints = 3;
			double halfArrow = this.bounds.height * 0.5d;
			double quarterArrow = halfArrow * 0.25d;
			double arrowX = this.bounds.width - halfArrow;
			this.arrow.xpoints = new int[] {(int) Math.round(this.bounds.x + arrowX - quarterArrow), (int) Math.round(this.bounds.x + arrowX + quarterArrow), (int) Math.round(this.bounds.x + arrowX - quarterArrow)};
			this.arrow.ypoints = new int[] {(int) Math.round(this.bounds.y + halfArrow - quarterArrow), (int) Math.round(this.bounds.y + halfArrow), (int) Math.round(this.bounds.y + halfArrow + quarterArrow)};
		}
	}

	void render(Area area) {
		area.smooth();
		area.color(this.border.value);
		area.fill(this.bounds);
		area.color(this.background.value);
		area.fill(this.backgroundRectangle);

		if(this.hasArrow) {
			area.color(this.border.value);
			area.fill(this.arrowRectangle);
			area.color(this.background.value);
			area.fill(this.arrowBackground);
			area.color(this.foreground.value);
			area.fill(this.arrow);
		}

		area.color(this.foreground.value);
		area.font(this.font);
		FontMetrics metrics = area.metrics();
		area.text(this.text, (int) Math.round(this.bounds.x + ((this.bounds.width - (this.hasArrow ? this.bounds.height : 0.0d)) - metrics.stringWidth(this.text)) * 0.5d), (int) Math.round(this.bounds.y + (this.bounds.height - metrics.getHeight()) * 0.5d + ((double) metrics.getLeading()) + ((double) metrics.getAscent())));
	}
}

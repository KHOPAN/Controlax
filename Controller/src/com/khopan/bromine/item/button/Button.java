package com.khopan.bromine.item.button;

import java.awt.Font;
import java.awt.geom.RoundRectangle2D;

import com.khopan.bromine.ModernItem;
import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.animation.interpolator.value.ColorInterpolator;
import com.khopan.bromine.animation.transform.DoubleTransform;
import com.khopan.bromine.area.Area;
import com.khopan.bromine.input.Mouse;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;
import com.khopan.bromine.theme.Theme;

public class Button extends ModernItem<Button> {
	private final RoundRectangle2D.Double rectangle;
	private final RoundRectangle2D.Double backgroundRectangle;
	private final DoubleTransform transform;
	private final ColorInterpolator foreground;
	private final ColorInterpolator background;
	private final ColorInterpolator border;

	private Runnable action;
	private boolean entered;
	private boolean pressed;

	private String text;
	private Font font;

	public Button() {
		this.rectangle = new RoundRectangle2D.Double();
		this.backgroundRectangle = new RoundRectangle2D.Double();
		this.foreground = new ColorInterpolator();
		this.background = new ColorInterpolator();
		this.border = new ColorInterpolator();
		this.foreground.value = this.theme.getColor(Theme.KEY_TEXT_COLOR);
		this.background.value = this.theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR);
		this.border.value = this.theme.getColor(Theme.KEY_ITEM_BORDER_COLOR);
		this.transform = new DoubleTransform();
		this.transform.duration().set(150);
		this.transform.value().set(0.0d);
		this.transform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.transform.framerate().set(240);
		this.transform.ticker().set(time -> {
			this.foreground.interpolate(time);
			this.background.interpolate(time);
			this.border.interpolate(time);
			this.update();
		});

		this.text = "";
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		this.naturalRoundnessCalculator = bounds -> Math.min(bounds.width, bounds.height) * 0.5d;
		this.naturalBorderThicknessCalculator = boubds -> Math.min(bounds.width, bounds.height) * 0.04d;
	}

	public Property<String, Button> text() {
		return new SimpleProperty<String, Button>(() -> this.text, text -> this.text = text, text -> this.update(), this).nullable().whenNull("");
	}

	public Property<Font, Button> font() {
		return new SimpleProperty<Font, Button>(() -> this.font, font -> this.font = font, font -> this.update(), this).nullable().whenNull(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
	}

	public Property<Runnable, Button> action() {
		return new SimpleProperty<Runnable, Button>(() -> this.action, onButtonClicked -> this.action = onButtonClicked, this).nullable();
	}

	@Override
	protected void onRoundnessUpdate(double roundness) {
		this.onResize();
		this.update();
	}

	@Override
	protected void onBorderThicknessUpdate(double borderThickness) {
		this.onResize();
		this.update();
	}

	@Override
	protected void onThemeUpdate(Theme theme) {
		if(this.entered) {
			if(this.pressed) {
				this.pressed();
			} else {
				this.hovered();
			}
		} else {
			this.normal();
		}
	}

	private void normal() {
		this.foreground.begin(this.theme.getColor(Theme.KEY_TEXT_COLOR));
		this.background.begin(this.theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR));
		this.border.begin(this.theme.getColor(Theme.KEY_ITEM_BORDER_COLOR));
		this.transform.begin(0.0d, 1.0d);
	}

	private void hovered() {
		this.foreground.begin(this.theme.getColor(Theme.KEY_HOVERED_TEXT_COLOR));
		this.background.begin(this.theme.getColor(Theme.KEY_HOVERED_ITEM_BACKGROUND_COLOR));
		this.border.begin(this.theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR));
		this.transform.begin(0.0d, 1.0d);
	}

	private void pressed() {
		this.foreground.begin(this.theme.getColor(Theme.KEY_PRESSED_TEXT_COLOR));
		this.background.begin(this.theme.getColor(Theme.KEY_PRESSED_ITEM_BACKGROUND_COLOR));
		this.border.begin(this.theme.getColor(Theme.KEY_PRESSED_ITEM_BORDER_COLOR));
		this.transform.begin(0.0d, 1.0d);
		this.transform.begin(0.0d, 1.0d);
	}

	@Override
	protected void mouseAction(Mouse mouse) {
		if(mouse.action == Mouse.ACTION_MOVED || mouse.action == Mouse.ACTION_ENTERED || mouse.action == Mouse.ACTION_EXITED) {
			if(this.rectangle.contains(mouse.location)) {
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

				if(this.action != null) {
					this.action.run();
				}

				this.hovered();
			}
		}
	}

	@Override
	protected void onResize() {
		super.onResize();
		this.rectangle.x = 0;
		this.rectangle.y = 0;
		this.rectangle.width = this.bounds.width;
		this.rectangle.height = this.bounds.height;
		this.rectangle.arcwidth = this.roundness;
		this.rectangle.archeight = this.roundness;
		this.backgroundRectangle.x = this.borderThickness;
		this.backgroundRectangle.y = this.borderThickness;
		this.backgroundRectangle.width = this.rectangle.width - this.borderThickness * 2;
		this.backgroundRectangle.height = this.rectangle.height - this.borderThickness * 2;
		this.backgroundRectangle.arcwidth = this.rectangle.arcwidth - this.borderThickness * 2;
		this.backgroundRectangle.archeight = this.rectangle.archeight - this.borderThickness * 2;
	}

	@Override
	protected void render(Area area) {
		area.smooth();
		area.font(this.font);
		area.color(this.border.value);
		area.fill(this.rectangle);
		area.color(this.background.value);
		area.fill(this.backgroundRectangle);
		area.color(this.foreground.value);
		area.textCenter(this.text);
	}
}

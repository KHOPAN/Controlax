package com.khopan.bromine;

import java.awt.Color;

import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.animation.transform.ColorTransform;
import com.khopan.bromine.area.Area;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;
import com.khopan.bromine.theme.Theme;

public abstract class PaneItem<T extends PaneItem<T>> extends RootItem<T> {
	private final ColorTransform transform;

	private Color backgroundColor;
	private boolean firstTime;

	private Color background;

	public PaneItem() {
		this.transform = new ColorTransform();
		this.transform.duration().set(200);
		this.transform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.transform.framerate().set(240);
		this.transform.ticker().set(color -> {
			this.backgroundColor = color;
			this.update();
		});
	}

	@Override
	protected void onThemeUpdate(Theme theme) {
		super.onThemeUpdate(theme);
		this.background().set(theme.getColor(Theme.KEY_BACKGROUND_COLOR));
	}

	@SuppressWarnings("unchecked")
	public Property<Color, T> background() {
		return new SimpleProperty<Color, T>(() -> this.background, background -> {
			this.background = background;

			if(this.background != null) {
				if(!this.firstTime) {
					this.firstTime = true;
					this.transform.begin(new Color(this.background.getRed(), this.background.getGreen(), this.background.getBlue(), 0), this.background);
				} else {
					this.transform.begin(this.background);
				}
			}
		}, (T) this).nullable();
	}

	@Override
	protected void render(Area area) {
		if(this.backgroundColor != null) {
			area.background(this.backgroundColor);
		}

		super.render(area);
	}
}

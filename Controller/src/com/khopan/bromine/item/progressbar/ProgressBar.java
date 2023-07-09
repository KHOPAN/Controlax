package com.khopan.bromine.item.progressbar;

import java.awt.geom.RoundRectangle2D;

import com.khopan.bromine.ModernItem;
import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.animation.interpolator.value.ColorInterpolator;
import com.khopan.bromine.animation.interpolator.value.DoubleInterpolator;
import com.khopan.bromine.animation.transform.DoubleTransform;
import com.khopan.bromine.area.Area;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;
import com.khopan.bromine.theme.Theme;

public class ProgressBar extends ModernItem<ProgressBar> {
	private final RoundRectangle2D.Double rectangle;
	private final RoundRectangle2D.Double backgroundRectangle;
	private final RoundRectangle2D.Double progressRectangle;
	private final DoubleInterpolator progressInterpolator;
	private final DoubleTransform progressTransform;
	private final ColorInterpolator foreground;
	private final ColorInterpolator background;
	private final ColorInterpolator border;
	private final DoubleTransform colorTransform;

	private double progress;

	public ProgressBar() {
		this.rectangle = new RoundRectangle2D.Double();
		this.backgroundRectangle = new RoundRectangle2D.Double();
		this.progressRectangle = new RoundRectangle2D.Double();
		this.progressInterpolator = new DoubleInterpolator();
		this.progressTransform = new DoubleTransform();
		this.progressTransform.duration().set(750);
		this.progressTransform.value().set(0.0d);
		this.progressTransform.interpolator().set(Interpolator.CUBIC_EASE_IN_OUT);
		this.progressTransform.framerate().set(240);
		this.progressTransform.ticker().set(time -> {
			this.progressInterpolator.interpolate(time);
			this.updateProgressRectangle();
			this.update();
		});

		this.foreground = new ColorInterpolator();
		this.background = new ColorInterpolator();
		this.border = new ColorInterpolator();
		this.foreground.value = this.theme.getColor(Theme.KEY_PRIMARY);
		this.background.value = this.theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR);
		this.border.value = this.theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR);
		this.colorTransform = new DoubleTransform();
		this.colorTransform.duration().set(150);
		this.colorTransform.value().set(0.0d);
		this.colorTransform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.colorTransform.framerate().set(240);
		this.colorTransform.ticker().set(time -> {
			this.foreground.interpolate(time);
			this.background.interpolate(time);
			this.border.interpolate(time);
			this.update();
		});

		this.naturalRoundnessCalculator = bounds -> Math.min(bounds.width, bounds.height) * 0.5d;
		this.naturalBorderThicknessCalculator = boubds -> Math.min(bounds.width, bounds.height) * 0.04d;
		this.progress = 0.0d;
	}

	@Override
	protected void onThemeUpdate(Theme theme) {
		if(this.visibility) {
			this.foreground.begin(this.theme.getColor(Theme.KEY_PRIMARY));
			this.background.begin(this.theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR));
			this.border.begin(this.theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR));
			this.colorTransform.begin(0.0d, 1.0d);
		} else {
			this.foreground.value = this.theme.getColor(Theme.KEY_PRIMARY);
			this.background.value = this.theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR);
			this.border.value = this.theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR);
		}
	}

	public Property<Double, ProgressBar> progress() {
		return new SimpleProperty<Double, ProgressBar>(() -> this.progress, progress -> {
			progress = Math.abs(progress);

			if(progress < 0.0d || progress > 1.0d) {
				throw new IllegalArgumentException("Progress value must be in between 0.0d and 1.0d");
			}

			this.progress = progress;

			if(this.visibility) {
				this.progressInterpolator.begin(this.progress);
				this.progressTransform.begin(0.0d, 1.0d);
			} else {
				this.progressInterpolator.value = this.progress;
				this.updateProgressRectangle();
				this.update();
			}
		}, this);
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
		this.updateProgressRectangle();
	}

	private void updateProgressRectangle() {
		this.progressRectangle.x = this.borderThickness;
		this.progressRectangle.y = this.borderThickness;
		this.progressRectangle.width = (this.rectangle.width - this.borderThickness * 2) * this.progressInterpolator.value;
		this.progressRectangle.height = this.rectangle.height - this.borderThickness * 2;
		this.progressRectangle.arcwidth = this.rectangle.arcwidth - this.borderThickness * 2;
		this.progressRectangle.archeight = this.rectangle.archeight - this.borderThickness * 2;
	}

	@Override
	protected void render(Area area) {
		area.smooth();
		area.color(this.border.value);
		area.fill(this.rectangle);
		area.color(this.background.value);
		area.fill(this.backgroundRectangle);
		area.color(this.foreground.value);
		area.fill(this.progressRectangle);
	}
}

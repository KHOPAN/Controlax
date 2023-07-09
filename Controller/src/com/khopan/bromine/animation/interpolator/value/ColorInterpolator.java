package com.khopan.bromine.animation.interpolator.value;

import java.awt.Color;

public class ColorInterpolator extends ValueInterpolator<Color> {
	public ColorInterpolator() {
		super(new Color(0x000000));
	}

	@Override
	public void interpolate(double time) {
		this.value = new Color(
				(int) Math.round(time * (((double) this.to.getRed()) - ((double) this.from.getRed())) + ((double) this.from.getRed())),
				(int) Math.round(time * (((double) this.to.getGreen()) - ((double) this.from.getGreen())) + ((double) this.from.getGreen())),
				(int) Math.round(time * (((double) this.to.getBlue()) - ((double) this.from.getBlue())) + ((double) this.from.getBlue())),
				(int) Math.round(time * (((double) this.to.getAlpha()) - ((double) this.from.getAlpha())) + ((double) this.from.getAlpha()))
				);
	}
}

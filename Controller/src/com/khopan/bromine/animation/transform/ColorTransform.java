package com.khopan.bromine.animation.transform;

import java.awt.Color;

import com.khopan.bromine.animation.Transform;

public class ColorTransform extends Transform<Color> {
	public ColorTransform() {
		super(new Color(0x000000));
	}

	@Override
	protected Color interpolate(double time, Color from, Color to) {
		return new Color(
				(int) Math.round(time * (((double) to.getRed()) - ((double) from.getRed())) + ((double) from.getRed())),
				(int) Math.round(time * (((double) to.getGreen()) - ((double) from.getGreen())) + ((double) from.getGreen())),
				(int) Math.round(time * (((double) to.getBlue()) - ((double) from.getBlue())) + ((double) from.getBlue())),
				(int) Math.round(time * (((double) to.getAlpha()) - ((double) from.getAlpha())) + ((double) from.getAlpha()))
				);
	}
}

package com.khopan.bromine.animation.transform.geometry;

import java.awt.Rectangle;

import com.khopan.bromine.animation.Transform;

public class RectangleTransform extends Transform<Rectangle> {
	public RectangleTransform() {
		super(new Rectangle(0, 0, 0, 0));
	}

	@Override
	protected Rectangle interpolate(double time, Rectangle from, Rectangle to) {
		return new Rectangle(
				(int) Math.round(time * (((double) to.x) - ((double) from.x)) + ((double) from.x)),
				(int) Math.round(time * (((double) to.y) - ((double) from.y)) + ((double) from.y)),
				(int) Math.round(time * (((double) to.width) - ((double) from.width)) + ((double) from.width)),
				(int) Math.round(time * (((double) to.height) - ((double) from.height)) + ((double) from.height))
				);
	}
}

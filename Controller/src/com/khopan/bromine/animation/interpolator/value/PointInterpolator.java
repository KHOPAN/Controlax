package com.khopan.bromine.animation.interpolator.value;

import java.awt.Point;

public class PointInterpolator extends ValueInterpolator<Point> {
	public PointInterpolator() {
		super(new Point(0, 0));
	}

	@Override
	public void interpolate(double time) {
		this.value = new Point(
				(int) Math.round(((double) this.from.x) + (((double) this.to.x) - ((double) this.from.x)) * time),
				(int) Math.round(((double) this.from.y) + (((double) this.to.y) - ((double) this.from.y)) * time)
				);
	}
}

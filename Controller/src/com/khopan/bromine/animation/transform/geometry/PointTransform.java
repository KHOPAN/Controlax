package com.khopan.bromine.animation.transform.geometry;

import java.awt.Point;

import com.khopan.bromine.animation.Transform;

public class PointTransform extends Transform<Point> {
	public PointTransform() {
		super(new Point(0, 0));
	}

	@Override
	protected Point interpolate(double time, Point from, Point to) {
		return new Point(
				(int) Math.round(time * (((double) to.x) - ((double) from.x)) + ((double) from.x)),
				(int) Math.round(time * (((double) to.y) - ((double) from.y)) + ((double) from.y))
				);
	}
}

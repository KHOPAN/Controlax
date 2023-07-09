package com.khopan.bromine.animation.transform;

import com.khopan.bromine.animation.Transform;

public class DoubleTransform extends Transform<Double> {
	public DoubleTransform() {
		super(0.0d);
	}

	@Override
	protected Double interpolate(double time, Double from, Double to) {
		return time * (to - from) + from;
	}
}

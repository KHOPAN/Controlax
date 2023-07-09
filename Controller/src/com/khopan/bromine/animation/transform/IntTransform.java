package com.khopan.bromine.animation.transform;

import com.khopan.bromine.animation.Transform;

public class IntTransform extends Transform<Integer> {
	public IntTransform() {
		super(0);
	}

	@Override
	protected Integer interpolate(double time, Integer from, Integer to) {
		return (int) Math.round(time * (((double) to) - ((double) from)) + ((double) from));
	}
}

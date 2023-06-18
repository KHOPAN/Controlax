package com.khopan.animation.transform;

import com.khopan.animation.Transform;

public class FloatTransform extends Transform<Float> {
	public FloatTransform() {
		super(0.0f);
	}

	@Override
	protected Float interpolate(double time, Float from, Float to) {
		return (float) (time * (((double) to) - ((double) from)) + ((double) from));
	}
}

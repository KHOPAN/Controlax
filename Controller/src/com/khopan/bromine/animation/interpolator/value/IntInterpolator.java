package com.khopan.bromine.animation.interpolator.value;

public class IntInterpolator extends ValueInterpolator<Integer> {
	public IntInterpolator() {
		super(0);
	}

	@Override
	public void interpolate(double time) {
		this.value = (int) Math.round(((double) this.from) + (((double) this.to) - ((double) this.from)) * time);
	}
}

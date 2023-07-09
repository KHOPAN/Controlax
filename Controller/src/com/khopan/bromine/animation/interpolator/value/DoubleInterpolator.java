package com.khopan.bromine.animation.interpolator.value;

public class DoubleInterpolator extends ValueInterpolator<Double> {
	public DoubleInterpolator() {
		super(0.0d);
	}

	@Override
	public void interpolate(double time) {
		this.value = this.from + (this.to - this.from) * time;
	}
}

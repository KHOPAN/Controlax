package com.khopan.bromine.animation.interpolator;

public class CompositeInterpolator extends Interpolator {
	private final Interpolator front;
	private final Interpolator middle;
	private final Interpolator back;

	public CompositeInterpolator(Interpolator side, Interpolator middle) {
		this(side, middle, side);
	}

	public CompositeInterpolator(Interpolator front, Interpolator middle, Interpolator back) {
		this.front = front;
		this.middle = middle;
		this.back = back;
	}

	@Override
	public double interpolate(double Time) {
		if(Time >= 0.0d && Time <= 0.333333333d) {
			return this.front.interpolate(Time);
		} else if(Time >= 0.333333333d && Time <= 0.666666667d) {
			return this.middle.interpolate(Time);
		} else if(Time >= 0.666666667d && Time <= 1.0d) {
			return this.back.interpolate(Time);
		} else {
			return Time;
		}
	}
}

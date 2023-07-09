package com.khopan.bromine.animation.interpolator.value;

public abstract class ValueInterpolator<T> {
	public T value;
	public T from;
	public T to;

	public ValueInterpolator(T defaultValue) {
		this.value = defaultValue;
		this.from = defaultValue;
		this.to = defaultValue;
	}

	public void begin(T to) {
		this.from = this.value;
		this.to = to;
	}

	public abstract void interpolate(double time);
}

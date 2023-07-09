package com.khopan.bromine.animation.interpolator;

public class MultiInterpolator extends Interpolator {
	private final Interpolator[] interpolators;
	private final boolean listNull;

	public MultiInterpolator(Interpolator... interpolators) {
		this.interpolators = interpolators;
		this.listNull = this.interpolators == null || this.interpolators.length == 0;
	}

	public MultiInterpolator() {
		this((Interpolator[]) null);
	}

	@Override
	public double interpolate(double Time) {
		if(this.listNull) {
			return Time;
		} else {
			for(int i = 0; i < this.interpolators.length; i++) {
				if(this.interpolators[i] == null) {
					continue;
				}

				Time = this.interpolators[i].interpolate(Time);
			}

			return Time;
		}
	}
}

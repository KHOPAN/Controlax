package com.khopan.animation.interpolator;

public abstract class Interpolator {
	public static final Interpolator DISCRETE = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return (Math.abs(Time - 1.0d) < 1.0E-12d) ? 1.0d : 0.0d;
		}
	};

	public static final Interpolator LINEAR = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return Time;
		}
	};

	public static final Interpolator EASE_IN = new EaseInterpolator() {
		@Override
		public double interpolate(double Time) {
			double Value = (Time < 0.2) ? EaseInterpolator.CONSTANT1 * Time * Time : EaseInterpolator.CONSTANT2 * Time - EaseInterpolator.CONSTANT5;
			return (Value < 0.0) ? 0.0 : (Value > 1.0) ? 1.0 : Value;
		}
	};

	public static final Interpolator EASE_OUT = new EaseInterpolator() {
		@Override
		public double interpolate(double Time) {
			double Value = (Time > 0.8) ? -EaseInterpolator.CONSTANT1 * Time * Time + EaseInterpolator.CONSTANT3 * Time - EaseInterpolator.CONSTANT4 : EaseInterpolator.CONSTANT2 * Time;
			return (Value < 0.0) ? 0.0 : (Value > 1.0) ? 1.0 : Value;
		}
	};

	public static final Interpolator EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			double Value = (Time < 0.2) ? 3.125 * Time * Time : (Time > 0.8) ? -3.125 * Time * Time + 6.25 * Time - 2.125 : 1.25 * Time - 0.125;
			return (Value < 0.0) ? 0.0 : (Value > 1.0) ? 1.0 : Value;
		}
	};

	/*
	 * This is the credit for interpolators:
	 * https://github.com/daimajia/AnimationEasingFunctions/tree/master/library/src/main/java/com/daimajia/easing
	 * Interpolators below might not exactly the same as the original, because it has been optimized.
	 */

	public static final Interpolator BACK_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return Time * Time * (2.0d * Time - 1.0d);
		}
	};

	public static final Interpolator BACK_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return ((Time -= 1.0d) * Time * (2.0d * Time + 1.0d) + 1.0d);
		}
	};

	public static final Interpolator BACK_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if((Time /= 0.5d) < 1.0d) {
				return 0.5d * (Time * Time * (2.0d * Time - 1.0d));
			}

			return 0.5d * ((Time -= 2) * Time * (2.0d * Time + 1.0d) + 2.0d);
		}
	};

	public static final Interpolator BOUNCE_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			Time = 1.0d - Time;

			if(Time < (1.0d / 2.75d)) {
				return 1.0d - (7.5625d * Time * Time);
			} else if(Time < (2.0d / 2.75d)) {
				return 1.0d - (7.5625d * (Time -= (1.5d / 2.75d)) * Time + 0.75d);
			} else if (Time < (2.5d / 2.75d)) {
				return 1.0d - (7.5625d * (Time -= (2.25d / 2.75d)) * Time + 0.9375d);
			} else { 
				return 1.0d - (7.5625d * (Time -= (2.625d / 2.75d)) * Time + 0.984375d);
			}
		}
	};

	public static final Interpolator BOUNCE_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if(Time < (1.0d / 2.75d)) {
				return (7.5625d * Time * Time);
			} else if(Time < (2.0d / 2.75d)) {
				return (7.5625d * (Time -= (1.5d / 2.75d)) * Time + 0.75d);
			} else if(Time < (2.5d / 2.75d)) {
				return (7.5625d * (Time -= (2.25d / 2.75d)) * Time + 0.9375d);
			} else {
				return (7.5625d * (Time -= (2.625d / 2.75d)) * Time + 0.984375d);
			}
		}
	};

	public static final Interpolator BOUNCE_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if(Time < 0.5d) {
				double InverseTime = (1.0d - (Time * 2));

				if(InverseTime < (1.0d / 2.75d)) {
					return (1.0d - (7.5625d * InverseTime * InverseTime)) * 0.5d;
				} else if(InverseTime < (2.0d / 2.75d)) {
					return (1.0d - (7.5625d * (InverseTime -= (1.5d / 2.75d)) * InverseTime + 0.75d)) * 0.5d;
				} else if (InverseTime < (2.5d / 2.75d)) {
					return (1.0d - (7.5625d * (InverseTime -= (2.25d / 2.75d)) * InverseTime + 0.9375d)) * 0.5d;
				} else { 
					return (1.0d - (7.5625d * (InverseTime -= (2.625d / 2.75d)) * InverseTime + 0.984375d)) * 0.5d;
				}
			} else {
				double NewTime = Time * 2.0d - 1.0d;

				if(NewTime < (1.0d / 2.75d)) {
					return (7.5625d * NewTime * NewTime) * 0.5d + 0.5d;
				} else if(NewTime < (2.0d / 2.75d)) {
					return (7.5625d * (NewTime -= (1.5d / 2.75d)) * NewTime + 0.75d) * 0.5d + 0.5d;
				} else if(NewTime < (2.5d / 2.75d)) {
					return (7.5625d * (NewTime -= (2.25d / 2.75d)) * NewTime + 0.9375d) * 0.5d + 0.5d;
				} else {
					return (7.5625d * (NewTime -= (2.625d / 2.75d)) * NewTime + 0.984375d) * 0.5d + 0.5d;
				}
			}
		}
	};

	public static final Interpolator CIRC_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return -1.0d * (Math.sqrt(1.0d - Time * Time) - 1.0d);
		}
	};

	public static final Interpolator CIRC_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return Math.sqrt(1.0d - (Time -= 1.0d) * Time);
		}
	};

	public static final Interpolator CIRC_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if((Time /= 1.0d / 2.0d) < 1.0d) {
				return -1.0d / 2.0d * (Math.sqrt(1 - Time * Time) - 1.0d);
			}

			return 1.0d / 2.0d * (Math.sqrt(1.0d - (Time -= 2.0d) * Time) + 1.0d);
		}
	};

	public static final Interpolator CUBIC_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return Time * Time * Time;
		}
	};

	public static final Interpolator CUBIC_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return ((Time -= 1.0d) * Time * Time + 1.0d);
		}
	};

	public static final Interpolator CUBIC_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if((Time /= 0.5d) < 1.0d) {
				return 0.5d * Time * Time * Time;
			}

			return 0.5d * ((Time -= 2.0d) * Time * Time + 2.0d);
		}
	};

	public static final Interpolator ELASTIC_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if(Time == 0.0d) {
				return 0.0d;
			}

			if(Time == 1.0d) {
				return 1.0d;
			}

			return -(Math.pow(2.0d, 10.0d * (Time -= 1)) * Math.sin((Time - (1.0d * 0.3d / 4.0d)) * (2.0d * Math.PI) / (1.0d * 0.3d)));
		}
	};

	public static final Interpolator ELASTIC_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if(Time == 0.0d) {
				return 0.0d;
			}

			if(Time == 1.0d) {
				return 1.0d;
			}

			return (Math.pow(2.0d, -10.0d * Time) * Math.sin((Time - (1.0d * 0.3d / 4.0d)) * (2.0d * Math.PI) / (1.0d * 0.3d)) + 1.0d);
		}
	};

	public static final Interpolator ELASTIC_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if(Time == 0.0d) {
				return 0.0d;
			}

			if((Time /= 0.5d) == 2.0d) {
				return 1.0d;
			}

			if(Time < 1.0d) {
				return -0.5d * (Math.pow(2.0d, 10.0d * (Time -= 1)) * Math.sin((Time - (1.0d * (0.3d * 1.5d) / 4.0d)) * (2.0d * Math.PI) / (1.0d * (0.3d * 1.5d))));
			}

			return Math.pow(2.0d, -10.0d * (Time -= 1)) * Math.sin((Time - (1.0d * (0.3d * 1.5d) / 4.0d)) * (2.0d * Math.PI) / (1.0d * (0.3d * 1.5d))) * 0.5d + 1.0d;
		}
	};

	public static final Interpolator EXPO_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return (Time == 0.0d) ? 0.0d : Math.pow(2.0d, 10.0d * (Time - 1.0d));
		}
	};

	public static final Interpolator EXPO_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return (Time == 1.0d) ? 1.0d : (-Math.pow(2.0d, -10.0d * Time) + 1.0d);
		}
	};

	public static final Interpolator EXPO_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if(Time == 0.0d) {
				return 0.0d;
			}

			if(Time == 1.0d) {
				return 1.0d;
			}

			if((Time /= 0.5d) < 1.0d) {
				return 0.5d * Math.pow(2.0d, 10.0d * (Time - 1.0d));
			}

			return 0.5d * (-Math.pow(2.0d, -10.0d * --Time) + 2.0d);
		}
	};

	public static final Interpolator QUAD_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return Time * Time;
		}
	};

	public static final Interpolator QUAD_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return -1.0d * Time * (Time - 2.0d);
		}
	};

	public static final Interpolator QUAD_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if((Time /= 0.5d) < 1.0d) {
				return 0.5d * Time * Time;
			}

			return -0.5d * ((--Time) * (Time - 2.0d) - 1.0d);
		}
	};

	public static final Interpolator QUINT_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return Time * Time * Time * Time * Time;
		}
	};

	public static final Interpolator QUINT_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return ((Time -= 1.0d) * Time * Time * Time * Time + 1.0d);
		}
	};

	public static final Interpolator QUINT_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			if((Time /= 0.5d) < 1.0d) {
				return 0.5d * Time * Time * Time * Time * Time;
			}

			return 0.5d * ((Time -= 2.0d) * Time * Time * Time * Time + 2.0d);
		}
	};

	public static final Interpolator SINE_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return -1.0d * Math.cos(Time * (Math.PI / 2.0d)) + 1.0d;
		}
	};

	public static final Interpolator SINE_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return Math.sin(Time * (Math.PI / 2.0d));
		}
	};

	public static final Interpolator SINE_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double Time) {
			return -0.5d * (Math.cos(Math.PI * Time) - 1.0d);
		}
	};

	/*
	 * This come from part of SmoothScroll at smoothscroll.net
	 */

	public static final Interpolator PULSE = new Interpolator() {
		private double pulseNormalize = 1.0d;
		private double pulseScale = 4.0d;

		private double pulse(double Time) {
			double result = 0.0d;
			Time *= this.pulseScale;

			if(Time < 1.0d) {
				result = Time - (1.0d - Math.exp(-Time));
			} else {
				double begin = Math.exp(-1.0d);
				result = begin + ((1.0d - Math.exp(-Time + 1.0d)) * (1.0d - begin));
			}

			return result * this.pulseNormalize;
		}

		@Override
		public double interpolate(double Time) {
			if(Time == 0.0d) {
				return 0.0d;
			} else if(Time == 1.0d) {
				return 1.0d;
			}

			if(this.pulseNormalize == 1.0d) {
				this.pulseNormalize /= this.pulse(1.0d);
			}

			return this.pulse(Time);
		}
	};

	public Interpolator() {

	}

	/**
	 * @param Time the time value mapped to 0.0d - 1.0d
	 * @return The value of time which mapped by that interpolator in the range of 0.0d - 1.0d
	 */
	public abstract double interpolate(double Time);

	private static abstract class EaseInterpolator extends Interpolator {
		private static final double CONSTANT1 = 25.0d / 9.0d;
		private static final double CONSTANT2 = 10.0d / 9.0d;
		private static final double CONSTANT3 = 50.0d / 9.0d;
		private static final double CONSTANT4 = 16.0d / 9.0d;
		private static final double CONSTANT5 = 1.0d / 9.0d;
	}
}

package com.khopan.bromine.animation.interpolator;

public abstract class Interpolator {
	public static final Interpolator DISCRETE = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return (Math.abs(time - 1.0d) < 1.0E-12d) ? 1.0d : 0.0d;
		}
	};

	public static final Interpolator LINEAR = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return time;
		}
	};

	public static final Interpolator EASE_IN = new EaseInterpolator() {
		@Override
		public double interpolate(double time) {
			double value = (time < 0.2) ? EaseInterpolator.CONSTANT1 * time * time : EaseInterpolator.CONSTANT2 * time - EaseInterpolator.CONSTANT5;
			return (value < 0.0) ? 0.0 : (value > 1.0) ? 1.0 : value;
		}
	};

	public static final Interpolator EASE_OUT = new EaseInterpolator() {
		@Override
		public double interpolate(double time) {
			double value = (time > 0.8) ? -EaseInterpolator.CONSTANT1 * time * time + EaseInterpolator.CONSTANT3 * time - EaseInterpolator.CONSTANT4 : EaseInterpolator.CONSTANT2 * time;
			return (value < 0.0) ? 0.0 : (value > 1.0) ? 1.0 : value;
		}
	};

	public static final Interpolator EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			double value = (time < 0.2) ? 3.125 * time * time : (time > 0.8) ? -3.125 * time * time + 6.25 * time - 2.125 : 1.25 * time - 0.125;
			return (value < 0.0) ? 0.0 : (value > 1.0) ? 1.0 : value;
		}
	};

	public static final Interpolator BACK_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return time * time * (2.0d * time - 1.0d);
		}
	};

	public static final Interpolator BACK_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return ((time -= 1.0d) * time * (2.0d * time + 1.0d) + 1.0d);
		}
	};

	public static final Interpolator BACK_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if((time /= 0.5d) < 1.0d) {
				return 0.5d * (time * time * (2.0d * time - 1.0d));
			}

			return 0.5d * ((time -= 2) * time * (2.0d * time + 1.0d) + 2.0d);
		}
	};

	public static final Interpolator BOUNCE_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double time) {
			time = 1.0d - time;

			if(time < (1.0d / 2.75d)) {
				return 1.0d - (7.5625d * time * time);
			} else if(time < (2.0d / 2.75d)) {
				return 1.0d - (7.5625d * (time -= (1.5d / 2.75d)) * time + 0.75d);
			} else if (time < (2.5d / 2.75d)) {
				return 1.0d - (7.5625d * (time -= (2.25d / 2.75d)) * time + 0.9375d);
			} else { 
				return 1.0d - (7.5625d * (time -= (2.625d / 2.75d)) * time + 0.984375d);
			}
		}
	};

	public static final Interpolator BOUNCE_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if(time < (1.0d / 2.75d)) {
				return (7.5625d * time * time);
			} else if(time < (2.0d / 2.75d)) {
				return (7.5625d * (time -= (1.5d / 2.75d)) * time + 0.75d);
			} else if(time < (2.5d / 2.75d)) {
				return (7.5625d * (time -= (2.25d / 2.75d)) * time + 0.9375d);
			} else {
				return (7.5625d * (time -= (2.625d / 2.75d)) * time + 0.984375d);
			}
		}
	};

	public static final Interpolator BOUNCE_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if(time < 0.5d) {
				double Inversetime = (1.0d - (time * 2));

				if(Inversetime < (1.0d / 2.75d)) {
					return (1.0d - (7.5625d * Inversetime * Inversetime)) * 0.5d;
				} else if(Inversetime < (2.0d / 2.75d)) {
					return (1.0d - (7.5625d * (Inversetime -= (1.5d / 2.75d)) * Inversetime + 0.75d)) * 0.5d;
				} else if (Inversetime < (2.5d / 2.75d)) {
					return (1.0d - (7.5625d * (Inversetime -= (2.25d / 2.75d)) * Inversetime + 0.9375d)) * 0.5d;
				} else { 
					return (1.0d - (7.5625d * (Inversetime -= (2.625d / 2.75d)) * Inversetime + 0.984375d)) * 0.5d;
				}
			} else {
				double Newtime = time * 2.0d - 1.0d;

				if(Newtime < (1.0d / 2.75d)) {
					return (7.5625d * Newtime * Newtime) * 0.5d + 0.5d;
				} else if(Newtime < (2.0d / 2.75d)) {
					return (7.5625d * (Newtime -= (1.5d / 2.75d)) * Newtime + 0.75d) * 0.5d + 0.5d;
				} else if(Newtime < (2.5d / 2.75d)) {
					return (7.5625d * (Newtime -= (2.25d / 2.75d)) * Newtime + 0.9375d) * 0.5d + 0.5d;
				} else {
					return (7.5625d * (Newtime -= (2.625d / 2.75d)) * Newtime + 0.984375d) * 0.5d + 0.5d;
				}
			}
		}
	};

	public static final Interpolator CIRC_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return -1.0d * (Math.sqrt(1.0d - time * time) - 1.0d);
		}
	};

	public static final Interpolator CIRC_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return Math.sqrt(1.0d - (time -= 1.0d) * time);
		}
	};

	public static final Interpolator CIRC_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if((time /= 1.0d / 2.0d) < 1.0d) {
				return -1.0d / 2.0d * (Math.sqrt(1 - time * time) - 1.0d);
			}

			return 1.0d / 2.0d * (Math.sqrt(1.0d - (time -= 2.0d) * time) + 1.0d);
		}
	};

	public static final Interpolator CUBIC_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return time * time * time;
		}
	};

	public static final Interpolator CUBIC_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return ((time -= 1.0d) * time * time + 1.0d);
		}
	};

	public static final Interpolator CUBIC_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if((time /= 0.5d) < 1.0d) {
				return 0.5d * time * time * time;
			}

			return 0.5d * ((time -= 2.0d) * time * time + 2.0d);
		}
	};

	public static final Interpolator ELASTIC_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if(time == 0.0d) {
				return 0.0d;
			}

			if(time == 1.0d) {
				return 1.0d;
			}

			return -(Math.pow(2.0d, 10.0d * (time -= 1)) * Math.sin((time - (1.0d * 0.3d / 4.0d)) * (2.0d * Math.PI) / (1.0d * 0.3d)));
		}
	};

	public static final Interpolator ELASTIC_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if(time == 0.0d) {
				return 0.0d;
			}

			if(time == 1.0d) {
				return 1.0d;
			}

			return (Math.pow(2.0d, -10.0d * time) * Math.sin((time - (1.0d * 0.3d / 4.0d)) * (2.0d * Math.PI) / (1.0d * 0.3d)) + 1.0d);
		}
	};

	public static final Interpolator ELASTIC_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if(time == 0.0d) {
				return 0.0d;
			}

			if((time /= 0.5d) == 2.0d) {
				return 1.0d;
			}

			if(time < 1.0d) {
				return -0.5d * (Math.pow(2.0d, 10.0d * (time -= 1)) * Math.sin((time - (1.0d * (0.3d * 1.5d) / 4.0d)) * (2.0d * Math.PI) / (1.0d * (0.3d * 1.5d))));
			}

			return Math.pow(2.0d, -10.0d * (time -= 1)) * Math.sin((time - (1.0d * (0.3d * 1.5d) / 4.0d)) * (2.0d * Math.PI) / (1.0d * (0.3d * 1.5d))) * 0.5d + 1.0d;
		}
	};

	public static final Interpolator EXPO_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return (time == 0.0d) ? 0.0d : Math.pow(2.0d, 10.0d * (time - 1.0d));
		}
	};

	public static final Interpolator EXPO_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return (time == 1.0d) ? 1.0d : (-Math.pow(2.0d, -10.0d * time) + 1.0d);
		}
	};

	public static final Interpolator EXPO_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if(time == 0.0d) {
				return 0.0d;
			}

			if(time == 1.0d) {
				return 1.0d;
			}

			if((time /= 0.5d) < 1.0d) {
				return 0.5d * Math.pow(2.0d, 10.0d * (time - 1.0d));
			}

			return 0.5d * (-Math.pow(2.0d, -10.0d * --time) + 2.0d);
		}
	};

	public static final Interpolator QUAD_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return time * time;
		}
	};

	public static final Interpolator QUAD_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return -1.0d * time * (time - 2.0d);
		}
	};

	public static final Interpolator QUAD_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if((time /= 0.5d) < 1.0d) {
				return 0.5d * time * time;
			}

			return -0.5d * ((--time) * (time - 2.0d) - 1.0d);
		}
	};

	public static final Interpolator QUINT_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return time * time * time * time * time;
		}
	};

	public static final Interpolator QUINT_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return ((time -= 1.0d) * time * time * time * time + 1.0d);
		}
	};

	public static final Interpolator QUINT_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			if((time /= 0.5d) < 1.0d) {
				return 0.5d * time * time * time * time * time;
			}

			return 0.5d * ((time -= 2.0d) * time * time * time * time + 2.0d);
		}
	};

	public static final Interpolator SINE_EASE_IN = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return -1.0d * Math.cos(time * (Math.PI / 2.0d)) + 1.0d;
		}
	};

	public static final Interpolator SINE_EASE_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return Math.sin(time * (Math.PI / 2.0d));
		}
	};

	public static final Interpolator SINE_EASE_IN_OUT = new Interpolator() {
		@Override
		public double interpolate(double time) {
			return -0.5d * (Math.cos(Math.PI * time) - 1.0d);
		}
	};

	public Interpolator() {

	}

	/**
	 * @param time the time value mapped to 0.0d - 1.0d
	 * @return The value of time which mapped by that interpolator in the range of 0.0d - 1.0d
	 */
	public abstract double interpolate(double time);

	private static abstract class EaseInterpolator extends Interpolator {
		private static final double CONSTANT1 = 25.0d / 9.0d;
		private static final double CONSTANT2 = 10.0d / 9.0d;
		private static final double CONSTANT3 = 50.0d / 9.0d;
		private static final double CONSTANT4 = 16.0d / 9.0d;
		private static final double CONSTANT5 = 1.0d / 9.0d;
	}
}

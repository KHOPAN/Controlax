package com.khopan.animation.interpolator;

public class CubicBezierInterpolator extends Interpolator {
	private final double x1;
	private final double y1;
	private final double x2;
	private final double y2;

	public CubicBezierInterpolator(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public double interpolate(double t) {
		// Calculate the interpolated value using the cubic Bezier formula.
		double u = 1 - t;
		double a = 3 * u * t;
		double b = 3 * (u * u - t * t);
		double c = 1 - 3 * u * u + 2 * t * t;
		double d = t * t;

		return a * x1 + b * y1 + c * x2 + d * y2;

		/*double cx = 3 * this.x1;
		double bx = 3 * (this.x2 - this.x1) - cx;
		double ax = 1 - cx - bx;
		double tSquared = Time * Time;
		double tCubed = tSquared * Time;
		return (ax * tCubed) + (bx * tSquared) + (cx * Time);*/
		//double x0 = 0, y0 = 0, x3 = 1, y3 = 1;
		//double cx = 3 * x1, bx = 3 * (x2 - x1) - cx, ax = 1 - cx - bx;
		//double cy = 3 * y1, by = 3 * (y2 - y1) - cy, ay = 1 - cy - by;

		// Evaluate the t value
		//double tSquared = Time * Time;
		//double tCubed = tSquared * Time;
		//double result = (ax * tCubed) + (bx * tSquared) + (cx * Time) + x0;
		//return result;
	}
}

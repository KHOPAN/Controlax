package com.khopan.bromine.area;

public class ColorFilter {
	public double filterRed;
	public double filterGreen;
	public double filterBlue;
	public double filterAlpha;

	public ColorFilter() {
		this.filterRed = 1.0d;
		this.filterGreen = 1.0d;
		this.filterBlue = 1.0d;
		this.filterAlpha = 1.0d;
	}

	public ColorFilter(double filterRed, double filterGreen, double filterBlue) {
		this.filterRed = filterRed;
		this.filterGreen = filterGreen;
		this.filterBlue = filterBlue;
		this.filterAlpha = 1.0d;
	}

	public ColorFilter(double filterRed, double filterGreen, double filterBlue, double filterAlpha) {
		this.filterRed = filterRed;
		this.filterGreen = filterGreen;
		this.filterBlue = filterBlue;
		this.filterAlpha = filterAlpha;
	}
}

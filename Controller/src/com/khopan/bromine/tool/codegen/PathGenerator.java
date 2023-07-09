package com.khopan.bromine.tool.codegen;

import java.awt.geom.PathIterator;

public class PathGenerator {
	private final double factor;
	private final String factorName;

	private String result;

	private PathGenerator(String variableName, PathIterator iterator, double factor, String factorName) {
		this.factor = 1.0d / factor;
		this.factorName = factorName;
		this.result = variableName + ".setWindingRule(" + (iterator.getWindingRule() == PathIterator.WIND_EVEN_ODD ? "PathIterator.WIND_EVEN_ODD" : "PathIterator.WIND_NON_ZERO") + ");\n";

		while(!iterator.isDone()) {
			double[] data = new double[6];
			int type = iterator.currentSegment(data);

			if(type == PathIterator.SEG_MOVETO) {
				this.result += variableName + ".moveTo(" + this.number(data[0]) + ", " + this.number(data[1]) + ");\n";
			} else if(type == PathIterator.SEG_LINETO) {
				this.result += variableName + ".lineTo(" + this.number(data[0]) + ", " + this.number(data[1]) + ");\n";
			} else if(type == PathIterator.SEG_QUADTO) {
				this.result += variableName + ".quadTo(" + this.number(data[0]) + ", " + this.number(data[1]) + ", " + this.number(data[2]) + ", " + this.number(data[3]) + ");\n";
			} else if(type == PathIterator.SEG_CUBICTO) {
				this.result += variableName + ".curveTo(" + this.number(data[0]) + ", " + this.number(data[1]) + ", " + this.number(data[2]) + ", " + this.number(data[3]) + ", " + this.number(data[4]) + ", " + this.number(data[5]) + ");\n";
			} else if(type == PathIterator.SEG_CLOSE) {
				this.result += variableName + ".closePath();\n";
			} else {
				throw new InternalError("Invalid path-segment type: " + type);
			}

			iterator.next();
		}
	}

	private String number(double number) {
		if(this.factorName != null) {
			return Double.toString(number * this.factor) + "d * " + this.factorName;
		} else {
			return Double.toString(number) + "d";
		}
	}

	public static String generatePath2D(String variableName, PathIterator iterator) {
		return new PathGenerator(variableName, iterator, 0.0d, null).result;
	}

	public static String generatePath2DFactor(String variableName, String factorName, double factor, PathIterator iterator) {
		return new PathGenerator(variableName, iterator, factor, factorName).result;
	}
}

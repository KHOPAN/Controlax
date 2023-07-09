package com.khopan.bromine.utils;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class Geometry {
	private Geometry() {}

	public static Shape createShadow(Shape shape, double shadowDistance) {
		Area shapeArea = new Area(shape);
		Area shadowArea = new Area(shape);
		AffineTransform transform = new AffineTransform();
		transform.translate(shadowDistance, shadowDistance);
		shadowArea.transform(transform);
		shadowArea.subtract(shapeArea);
		return shadowArea;
	}

	public static Shape subtractShapes(Shape... shapes) {
		if(shapes != null && shapes.length > 1) {
			Area area = new Area(shapes[0]);

			for(int i = 1; i < shapes.length; i++) {
				area.subtract(new Area(shapes[i]));
			}

			return area;
		}

		return null;
	}

	public static Shape subtractShape(Shape x, Shape y) {
		Area area = new Area(x);
		area.subtract(new Area(y));
		return area;
	}
}

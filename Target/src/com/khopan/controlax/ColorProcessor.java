package com.khopan.controlax;

import com.khopan.controlax.ui.color.ColorDisplay;
import com.khopan.lazel.config.BinaryConfigObject;

public class ColorProcessor {
	public static void processColor(BinaryConfigObject config) {
		int action = config.getInt("SubAction");

		if(action == 0) {
			ColorDisplay.clear();
		} else if(action == 1) {
			int color = config.getInt("Color");
			float transparency = config.getFloat("Transparency");
			ColorDisplay.transparency(transparency);
			ColorDisplay.display(color);
		} else if(action == 2) {
			boolean moving = config.getBoolean("Moving");
			float transparency = config.getFloat("Transparency");
			float rate = config.getFloat("Rate");
			ColorDisplay.transparency(transparency);
			ColorDisplay.rainbowRate(rate);
			ColorDisplay.rainbow(moving);
		}
	}
}

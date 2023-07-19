package com.khopan.controlax.processor;

import com.khopan.controlax.action.action.ColorAction;
import com.khopan.controlax.ui.color.ColorDisplay;

public class ColorProcessor {
	public static void process(ColorAction action) {
		int code = action.getAction();

		if(code == ColorAction.ACTION_SET_COLOR) {
			ColorDisplay.transparency(action.getTransparency());
			ColorDisplay.display(action.getColor());
		} else if(code == ColorAction.ACTION_CLEAR_COLOR) {
			ColorDisplay.clear();
		} else if(code == ColorAction.ACTION_MOVING_RAINBOW) {
			ColorDisplay.transparency(action.getTransparency());
			ColorDisplay.rainbowRate(action.getMovingRate());
			ColorDisplay.rainbow(true);
		} else if(code == ColorAction.ACTION_RAINBOW) {
			ColorDisplay.transparency(action.getTransparency());
			ColorDisplay.rainbowRate(action.getMovingRate());
			ColorDisplay.rainbow(false);
		}
	}
}

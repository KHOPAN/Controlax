package com.khopan.controlax;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.khopan.lazel.config.BinaryConfigObject;

public class ControllingProcessor {
	private static final Dimension DISPLAY_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();

	private static boolean MouseControl;
	private static boolean KeyboardControl;

	public static void process(BinaryConfigObject config) {
		int action = config.getInt("SubAction");

		if(action == 1) {
			ControllingProcessor.MouseControl = config.getBoolean("Control");
		} else if(action == 2) {
			ControllingProcessor.KeyboardControl = config.getBoolean("Control");
		} else if(action == 3) {
			if(ControllingProcessor.MouseControl) {
				int x = (int) Math.round(config.getDouble("MouseX") * ((double) ControllingProcessor.DISPLAY_DIMENSION.width));
				int y = (int) Math.round(config.getDouble("MouseY") * ((double) ControllingProcessor.DISPLAY_DIMENSION.height));
				Controlax.INSTANCE.robot.mouseMove(x, y);
			}
		} else if(action == 4) {
			if(ControllingProcessor.MouseControl) {
				Controlax.INSTANCE.robot.mousePress(config.getInt("Button"));
			}
		} else if(action == 5) {
			if(ControllingProcessor.MouseControl) {
				Controlax.INSTANCE.robot.mouseRelease(config.getInt("Button"));
			}
		} else if(action == 6) {
			if(ControllingProcessor.MouseControl) {
				Controlax.INSTANCE.robot.mouseWheel(config.getInt("Amount"));
			}
		} else if(action == 7) {
			if(ControllingProcessor.KeyboardControl) {
				Controlax.INSTANCE.robot.keyPress(config.getInt("KeyCode"));
			}
		} else if(action == 8) {
			if(ControllingProcessor.KeyboardControl) {
				Controlax.INSTANCE.robot.keyRelease(config.getInt("KeyCode"));
			}
		}
	}
}

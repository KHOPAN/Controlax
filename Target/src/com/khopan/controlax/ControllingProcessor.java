package com.khopan.controlax;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.khopan.controlax.action.action.KeyboardAction;
import com.khopan.controlax.action.action.MouseAction;

public class ControllingProcessor {
	private static final Dimension DISPLAY_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();

	public static void mouse(MouseAction action) {
		int code = action.getAction();

		if(code == MouseAction.ACTION_MOUSE_WHEEL_MOVED) {
			Controlax.INSTANCE.robot.mouseWheel((int) Math.round(action.getWheelRotation()));
		} else {
			int x = (int) Math.round(action.getX() * ((double) ControllingProcessor.DISPLAY_DIMENSION.width));
			int y = (int) Math.round(action.getY() * ((double) ControllingProcessor.DISPLAY_DIMENSION.height));
			Controlax.INSTANCE.robot.mouseMove(x, y);

			try {
				Thread.sleep(68);
			} catch(Throwable Errors) {

			}

			if(code == MouseAction.ACTION_MOUSE_PRESSED) {
				Controlax.INSTANCE.robot.mousePress(action.getButton());
			} else if(code == MouseAction.ACTION_MOUSE_PRESSED) {
				Controlax.INSTANCE.robot.mouseRelease(action.getButton());
			}
		}
	}

	public static void keyboard(KeyboardAction action) {
		int code = action.getAction();

		if(code == KeyboardAction.ACTION_KEY_PRESSED) {
			Controlax.INSTANCE.robot.keyPress(action.getKeyCode());
		} else if(code == KeyboardAction.ACTION_KEY_RELEASED) {
			Controlax.INSTANCE.robot.keyRelease(action.getKeyCode());
		}
	}
}

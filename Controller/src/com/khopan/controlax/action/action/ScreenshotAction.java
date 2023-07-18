package com.khopan.controlax.action.action;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class ScreenshotAction extends Action {
	private ScreenshotAction() {

	}

	private ScreenshotAction(BinaryConfigObject config) {

	}

	public static ScreenshotAction getInstance() {
		return new ScreenshotAction();
	}
}

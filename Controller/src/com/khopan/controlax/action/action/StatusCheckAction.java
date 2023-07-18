package com.khopan.controlax.action.action;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class StatusCheckAction extends Action {
	private StatusCheckAction() {

	}

	private StatusCheckAction(BinaryConfigObject config) {

	}

	public static StatusCheckAction getInstance() {
		return new StatusCheckAction();
	}
}

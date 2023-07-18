package com.khopan.controlax.action.action;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class EmergencyTerminateAction extends Action {
	private EmergencyTerminateAction() {

	}

	private EmergencyTerminateAction(BinaryConfigObject config) {

	}

	public static EmergencyTerminateAction getInstance() {
		return new EmergencyTerminateAction();
	}
}

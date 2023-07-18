package com.khopan.controlax.action.action;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class TestTargetAction extends Action {
	private TestTargetAction() {

	}

	private TestTargetAction(BinaryConfigObject config) {

	}

	public static TestTargetAction getInstance() {
		return new TestTargetAction();
	}
}

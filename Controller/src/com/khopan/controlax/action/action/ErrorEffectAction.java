package com.khopan.controlax.action.action;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class ErrorEffectAction extends Action {
	private ErrorEffectAction() {

	}

	private ErrorEffectAction(BinaryConfigObject config) {

	}

	public static ErrorEffectAction getInstance() {
		return new ErrorEffectAction();
	}
}

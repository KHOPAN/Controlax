package com.khopan.controlax.action.action;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class MessageAction extends Action {
	private final String message;

	private MessageAction(String message) {
		this.message = message == null ? "" : message;
	}

	private MessageAction(BinaryConfigObject config) {
		if(!config.map().containsKey("Message")) {
			throw new IllegalArgumentException("Broken ResponseAction object, key 'Response' missing");
		}

		this.message = config.getString("Message");
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public void actionData(BinaryConfigObject config) {
		config.putString("Message", this.message);
	}

	public static MessageAction getInstance(String message) {
		return new MessageAction(message);
	}
}

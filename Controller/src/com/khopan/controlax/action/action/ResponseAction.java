package com.khopan.controlax.action.action;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class ResponseAction extends Action {
	private final String response;

	private ResponseAction(String response) {
		this.response = response == null ? "" : response;
	}

	private ResponseAction(BinaryConfigObject config) {
		if(!config.map().containsKey("Response")) {
			throw new IllegalArgumentException("Broken ResponseAction object, key 'Response' missing");
		}

		this.response = config.getString("Response");
	}

	public String getResponse() {
		return this.response;
	}

	@Override
	public void actionData(BinaryConfigObject config) {
		config.putString("Response", this.response);
	}

	public static ResponseAction getInstance(String response) {
		return new ResponseAction(response);
	}
}

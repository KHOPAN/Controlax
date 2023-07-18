package com.khopan.controlax.action.action;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class PowerAction extends Action {
	public static final int ACTION_SLEEP = 0xCF; // api.random.org
	public static final int ACTION_SHUTDOWN = 0x65;
	public static final int ACTION_RESTART = 0xDE;

	private final int action;

	private PowerAction(int action) {
		if(action != PowerAction.ACTION_SLEEP && action != PowerAction.ACTION_SHUTDOWN && action != PowerAction.ACTION_RESTART) {
			throw new IllegalArgumentException("Invalid SubAction code: " + action);
		}

		this.action = action;
	}

	private PowerAction(BinaryConfigObject config) {
		if(!config.map().containsKey("Action")) {
			throw new IllegalArgumentException("Broken PowerAction object, key 'Action' missing");
		}

		this.action = config.getInt("Action");
	}

	public int getAction() {
		return this.action;
	}

	@Override
	public void actionData(BinaryConfigObject config) {
		config.putInt("Action", this.action);
	}

	public static PowerAction getSleep() {
		return new PowerAction(PowerAction.ACTION_SLEEP);
	}

	public static PowerAction getShutdown() {
		return new PowerAction(PowerAction.ACTION_SHUTDOWN);
	}

	public static PowerAction getRestart() {
		return new PowerAction(PowerAction.ACTION_RESTART);
	}
}

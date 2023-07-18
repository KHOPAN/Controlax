package com.khopan.controlax.action.action;

import java.util.Map;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class KeyboardAction extends Action {
	public static final int ACTION_KEY_PRESSED = 0x8C; // api.random.org
	public static final int ACTION_KEY_RELEASED = 0xDC;

	private final int action;
	private final int keyCode;

	private KeyboardAction(int action, int keyCode) {
		this.action = action;
		this.keyCode = keyCode;
	}

	private KeyboardAction(BinaryConfigObject config) {
		Map<String, Object> map = config.map();

		if(!map.containsKey("Action")) {
			throw new IllegalArgumentException("Broken KeyboardAction object, key 'Action' missing");
		}

		if(!map.containsKey("KeyCode")) {
			throw new IllegalArgumentException("Broken KeyboardAction object, key 'KeyCode' missing");
		}

		this.action = config.getInt("Action");
		this.keyCode = config.getInt("KeyCode");
	}

	public int getAction() {
		return this.action;
	}

	public int getKeyCode() {
		return this.keyCode;
	}

	@Override
	public void actionData(BinaryConfigObject config) {
		config.putInt("Action", this.action);
		config.putInt("KeyCode", this.getKeyCode());
	}

	public static KeyboardAction getKeyPressed(int keyCode) {
		return new KeyboardAction(KeyboardAction.ACTION_KEY_PRESSED, keyCode);
	}

	public static KeyboardAction getKeyReleased(int keyCode) {
		return new KeyboardAction(KeyboardAction.ACTION_KEY_RELEASED, keyCode);
	}
}

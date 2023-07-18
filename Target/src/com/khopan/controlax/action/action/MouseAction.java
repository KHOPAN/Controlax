package com.khopan.controlax.action.action;

import java.util.Map;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class MouseAction extends Action {
	public static final int ACTION_MOUSE_MOVED = 0x15; // api.random.org
	public static final int ACTION_MOUSE_PRESSED = 0x56;
	public static final int ACTION_MOUSE_RELEASED = 0x21;
	public static final int ACTION_MOUSE_WHEEL_MOVED = 0xFB;

	private final int action;
	private final double x;
	private final double y;
	private final int button;
	private final double scrollAmount;

	private MouseAction(int action, double x, double y, int button, double scrollAmount) {
		this.action = action;
		this.x = x;
		this.y = y;
		this.button = button;
		this.scrollAmount = scrollAmount;
	}

	private MouseAction(BinaryConfigObject config) {
		Map<String, Object> map = config.map();

		if(!map.containsKey("Action")) {
			throw new IllegalArgumentException("Broken MouseAction object, key 'Action' missing");
		}

		this.action = config.getInt("Action");

		if(this.action == MouseAction.ACTION_MOUSE_WHEEL_MOVED) {
			if(!map.containsKey("ScrollAmount")) {
				throw new IllegalArgumentException("Broken MouseAction object, key 'ScrollAmount' missing");
			}

			this.x = 0;
			this.y = 0;
			this.button = 0;
			this.scrollAmount = config.getDouble("ScrollAmount");
		} else {
			if(!map.containsKey("LocationX")) {
				throw new IllegalArgumentException("Broken MouseAction object, key 'LocationX' missing");
			}

			if(!map.containsKey("LocationY")) {
				throw new IllegalArgumentException("Broken MouseAction object, key 'LocationY' missing");
			}

			this.x = config.getDouble("LocationX");
			this.y = config.getDouble("LocationY");

			if(this.action != MouseAction.ACTION_MOUSE_MOVED) {
				if(!map.containsKey("Button")) {
					throw new IllegalArgumentException("Broken MouseAction object, key 'Button' missing");
				}

				this.button = config.getInt("Button");
			} else {
				this.button = 0;
			}

			this.scrollAmount = 0.0d;
		}
	}

	public int getAction() {
		return this.action;
	}

	public double getX() {
		if(this.action == MouseAction.ACTION_MOUSE_WHEEL_MOVED) {
			throw new IllegalStateException("This SubAction does not support Location");
		}

		return this.x;
	}

	public double getY() {
		if(this.action == MouseAction.ACTION_MOUSE_WHEEL_MOVED) {
			throw new IllegalStateException("This SubAction does not support Location");
		}

		return this.y;
	}

	public int getButton() {
		if(this.action == MouseAction.ACTION_MOUSE_MOVED || this.action == MouseAction.ACTION_MOUSE_WHEEL_MOVED) {
			throw new IllegalStateException("This SubAction does not support Button");
		}

		return this.button;
	}

	public double getWheelRotation() {
		if(this.action != MouseAction.ACTION_MOUSE_WHEEL_MOVED) {
			throw new IllegalStateException("This SubAction does not support Mouse Wheel");
		}

		return this.scrollAmount;
	}

	@Override
	public void actionData(BinaryConfigObject config) {
		config.putInt("Action", this.action);

		if(this.action == MouseAction.ACTION_MOUSE_WHEEL_MOVED) {
			config.putDouble("ScrollAmount", this.scrollAmount);
		} else {
			config.putDouble("LocationX", this.x);
			config.putDouble("LocationY", this.y);

			if(this.action != MouseAction.ACTION_MOUSE_MOVED) {
				config.putInt("Button", this.button);
			}
		}
	}

	public static MouseAction getMouseMoved(double x, double y) {
		return new MouseAction(MouseAction.ACTION_MOUSE_MOVED, x, y, 0, 0.0d);
	}

	public static MouseAction getMousePressed(double x, double y, int button) {
		return new MouseAction(MouseAction.ACTION_MOUSE_PRESSED, x, y, button, 0.0d);
	}

	public static MouseAction getMouseReleased(double x, double y, int button) {
		return new MouseAction(MouseAction.ACTION_MOUSE_RELEASED, x, y, button, 0.0d);
	}

	public static MouseAction getMouseWheelMoved(double scrollAmount) {
		return new MouseAction(MouseAction.ACTION_MOUSE_WHEEL_MOVED, 0, 0, 0, scrollAmount);
	}
}

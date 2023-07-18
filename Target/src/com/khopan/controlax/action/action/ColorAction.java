package com.khopan.controlax.action.action;

import java.util.Map;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class ColorAction extends Action {
	public static final int ACTION_SET_COLOR = 0x7B; // api.random.org
	public static final int ACTION_CLEAR_COLOR = 0x1C;
	public static final int ACTION_MOVING_RAINBOW = 0x35;
	public static final int ACTION_RAINBOW = 0x76;

	private final int action;
	private final int color;
	private final double transparency;
	private final boolean moving;
	private final double movingRate;

	private ColorAction(int action, int color, double transparency, boolean moving, double movingRate) {
		if(action != ColorAction.ACTION_SET_COLOR && action != ColorAction.ACTION_CLEAR_COLOR && action != ColorAction.ACTION_MOVING_RAINBOW && action != ColorAction.ACTION_RAINBOW) {
			throw new IllegalArgumentException("Invalid SubAction code: " + action);
		}

		this.action = action;
		this.color = color;
		this.transparency = transparency;
		this.moving = moving;
		this.movingRate = movingRate;
	}

	private ColorAction(BinaryConfigObject config) {
		Map<String, Object> map = config.map();

		if(!map.containsKey("Action")) {
			throw new IllegalArgumentException("Broken ColorAction object, key 'Action' missing");
		}

		if(!map.containsKey("Color")) {
			throw new IllegalArgumentException("Broken ColorAction object, key 'Color' missing");
		}

		if(!map.containsKey("Transparency")) {
			throw new IllegalArgumentException("Broken ColorAction object, key 'Transparency' missing");
		}

		if(!map.containsKey("Moving")) {
			throw new IllegalArgumentException("Broken ColorAction object, key 'Moving' missing");
		}

		if(!map.containsKey("MovingRate")) {
			throw new IllegalArgumentException("Broken ColorAction object, key 'MovingRate' missing");
		}

		this.action = config.getInt("Action");

		if(this.action != ColorAction.ACTION_SET_COLOR && this.action != ColorAction.ACTION_CLEAR_COLOR && this.action != ColorAction.ACTION_MOVING_RAINBOW && this.action != ColorAction.ACTION_RAINBOW) {
			throw new IllegalArgumentException("Invalid SubAction code: " + this.action);
		}

		this.color = config.getInt("Color");
		this.transparency = config.getDouble("Transparency");
		this.moving = config.getBoolean("Moving");
		this.movingRate = config.getDouble("MovingRate");
	}

	public int getAction() {
		return this.action;
	}

	public int getColor() {
		if(this.action != ColorAction.ACTION_SET_COLOR) {
			throw new IllegalStateException("This SubAction does not support Color");
		}

		return this.color;
	}

	public double getTransparency() {
		if(this.action == ColorAction.ACTION_CLEAR_COLOR) {
			throw new IllegalStateException("This SubAction does not support Transparency");
		}

		return this.transparency;
	}

	public boolean isMoving() {
		return this.moving;
	}

	public double getMovingRate() {
		if(this.action != ColorAction.ACTION_MOVING_RAINBOW) {
			throw new IllegalStateException("This SubAction does not support rainbow Moving Rate");
		}

		return this.movingRate;
	}

	@Override
	public void actionData(BinaryConfigObject config) {
		config.putInt("Action", this.action);

		if(this.action == ColorAction.ACTION_SET_COLOR) {
			config.putInt("Color", this.color);
		}

		if(this.action != ColorAction.ACTION_CLEAR_COLOR) {
			config.putDouble("Transparency", this.transparency);
		}

		config.putBoolean("Moving", this.moving);

		if(this.action == ColorAction.ACTION_MOVING_RAINBOW) {
			config.putDouble("MovingRate", this.movingRate);
		}
	}

	public static ColorAction getSetColor(int color, double transparency) {
		return new ColorAction(ColorAction.ACTION_SET_COLOR, color, transparency, false, 0.0d);
	}

	public static ColorAction getClearColor() {
		return new ColorAction(ColorAction.ACTION_CLEAR_COLOR, -1, 0.0d, false, 0.0d);
	}

	public static ColorAction getMovingRainbow(double transparency, double movingRate) {
		return new ColorAction(ColorAction.ACTION_MOVING_RAINBOW, -1, transparency, true, movingRate);
	}

	public static ColorAction getRainbow(double transparency) {
		return new ColorAction(ColorAction.ACTION_MOVING_RAINBOW, -1, transparency, false, 0.0d);
	}
}

package com.khopan.bromine.input;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Mouse {
	public static final int ACTION_DRAGGED = MouseEvent.MOUSE_DRAGGED;
	public static final int ACTION_MOVED = MouseEvent.MOUSE_MOVED;
	public static final int ACTION_CLICKED = MouseEvent.MOUSE_CLICKED;
	public static final int ACTION_PRESSED = MouseEvent.MOUSE_PRESSED;
	public static final int ACTION_RELEASED = MouseEvent.MOUSE_RELEASED;
	public static final int ACTION_ENTERED = MouseEvent.MOUSE_ENTERED;
	public static final int ACTION_EXITED = MouseEvent.MOUSE_EXITED;
	public static final int ACTION_WHEEL_MOVED = MouseWheelEvent.MOUSE_WHEEL;

	public static final int TYPE_WHEEL_UNIT_SCROLL = MouseWheelEvent.WHEEL_UNIT_SCROLL;
	public static final int TYPE_WHEEL_BLOCK_SCROLL = MouseWheelEvent.WHEEL_BLOCK_SCROLL;

	public static final int LEFT_MOUSE_BUTTON = MouseEvent.BUTTON1;
	public static final int MIDDLE_MOUSE_BUTTON = MouseEvent.BUTTON2;
	public static final int RIGHT_MOUSE_BUTTON = MouseEvent.BUTTON3;

	public final int button;
	public final int clickCount;
	public final int action;
	public final Point screenLocation;
	public final int screenX;
	public final int screenY;
	public final int modifiers;
	public final int extendedModifiers;
	public final Point location;
	public final long time;
	public final int x;
	public final int y;

	public final int scrollType;
	public final int scrollAmount;
	public final int wheelRotation;
	public final double preciseWheelRotation;

	public final boolean isLeftButton;
	public final boolean isMiddleButton;
	public final boolean isRightButton;

	@SuppressWarnings("deprecation")
	public Mouse(MouseEvent Event) {
		this.button = Event.getButton();
		this.clickCount = Event.getClickCount();
		this.action = Event.getID();
		this.screenLocation = Event.getLocationOnScreen();
		this.screenX = this.screenLocation.x;
		this.screenY = this.screenLocation.y;
		this.modifiers = Event.getModifiers();
		this.extendedModifiers = Event.getModifiersEx();
		this.location = Event.getPoint();
		this.time = Event.getWhen();
		this.x = this.location.x;
		this.y = this.location.y;

		if(Event instanceof MouseWheelEvent wheel) {
			this.scrollType = wheel.getScrollType();
			this.scrollAmount = wheel.getScrollAmount();
			this.wheelRotation = wheel.getWheelRotation();
			this.preciseWheelRotation = wheel.getPreciseWheelRotation();
		} else {
			this.scrollType = 0;
			this.scrollAmount = 0;
			this.wheelRotation = 0;
			this.preciseWheelRotation = 0.0d;
		}

		this.isLeftButton = this.checkMouseButton(Mouse.LEFT_MOUSE_BUTTON, InputEvent.BUTTON1_DOWN_MASK);
		this.isMiddleButton = this.checkMouseButton(Mouse.MIDDLE_MOUSE_BUTTON, InputEvent.BUTTON2_DOWN_MASK);
		this.isRightButton = this.checkMouseButton(Mouse.RIGHT_MOUSE_BUTTON, InputEvent.BUTTON3_DOWN_MASK);
	}

	public Mouse(int button, int clickCount, int action, Point screenLocation, int modifiers, int extendedModifiers, Point location, long time, int scrollType, int scrollAmount, int wheelRotation, double preciseWheelRotation) {
		this.button = button;
		this.clickCount = clickCount;
		this.action = action;
		this.screenLocation = screenLocation;
		this.screenX = this.screenLocation.x;
		this.screenY = this.screenLocation.y;
		this.modifiers = modifiers;
		this.extendedModifiers = extendedModifiers;
		this.location = location;
		this.time = time;
		this.x = this.location.x;
		this.y = this.location.y;
		this.scrollType = scrollType;
		this.scrollAmount = scrollAmount;
		this.wheelRotation = wheelRotation;
		this.preciseWheelRotation = preciseWheelRotation;
		this.isLeftButton = this.checkMouseButton(Mouse.LEFT_MOUSE_BUTTON, InputEvent.BUTTON1_DOWN_MASK);
		this.isMiddleButton = this.checkMouseButton(Mouse.MIDDLE_MOUSE_BUTTON, InputEvent.BUTTON2_DOWN_MASK);
		this.isRightButton = this.checkMouseButton(Mouse.RIGHT_MOUSE_BUTTON, InputEvent.BUTTON3_DOWN_MASK);
	}

	private boolean checkMouseButton(int button, int mask) {
		switch(this.action) {
		case Mouse.ACTION_CLICKED:
		case Mouse.ACTION_PRESSED:
		case Mouse.ACTION_RELEASED:
			return this.button == button;
		case Mouse.ACTION_DRAGGED:
		case Mouse.ACTION_ENTERED:
		case Mouse.ACTION_EXITED:
			return ((this.extendedModifiers & mask) != 0);
		default:
			return ((this.extendedModifiers & mask) != 0) || this.button == button;
		}
	}
}

package com.khopan.bromine.input;

import java.awt.event.KeyEvent;

public class Keyboard {
	public static final int ACTION_TYPED = KeyEvent.KEY_TYPED;
	public static final int ACTION_PRESSED = KeyEvent.KEY_PRESSED;
	public static final int ACTION_RELEASED = KeyEvent.KEY_RELEASED;

	public final int extendedKeyCode;
	public final int action;
	public final char keyCharacter;
	public final int keyCode;
	public final int keyLocation;
	public final int modifiers;
	public final int extendedModifiers;
	public final boolean actionKey;
	public final long time;

	@SuppressWarnings("deprecation")
	public Keyboard(KeyEvent Event) {
		this.extendedKeyCode = Event.getExtendedKeyCode();
		this.action = Event.getID();
		this.keyCharacter = Event.getKeyChar();
		this.keyCode = Event.getKeyCode();
		this.keyLocation = Event.getKeyLocation();
		this.modifiers = Event.getModifiers();
		this.extendedModifiers = Event.getModifiersEx();
		this.time = Event.getWhen();
		this.actionKey = Event.isActionKey();
	}

	public Keyboard(int extendedKeyCode, int action, char keyCharacter, int keyCode, int keyLocation, int modifiers, int extendedModifiers, long time, boolean actionKey) {
		this.extendedKeyCode = extendedKeyCode;
		this.action = action;
		this.keyCharacter = keyCharacter;
		this.keyCode = keyCode;
		this.keyLocation = keyLocation;
		this.modifiers = modifiers;
		this.extendedModifiers = extendedModifiers;
		this.time = time;
		this.actionKey = actionKey;
	}
}

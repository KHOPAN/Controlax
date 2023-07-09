package com.khopan.bromine.theme;

import java.awt.Color;

public class LightTheme extends Theme {
	public static final LightTheme THEME = new LightTheme();

	private LightTheme() {
		this.addKey(Theme.KEY_TEXT_COLOR, new Color(0x000000));
		this.addKey(Theme.KEY_HOVERED_TEXT_COLOR, new Color(0x000000));
		this.addKey(Theme.KEY_PRESSED_TEXT_COLOR, new Color(0x000000));
		this.addKey(Theme.KEY_ITEM_BACKGROUND_COLOR, new Color(0xCCCCCC));
		this.addKey(Theme.KEY_HOVERED_ITEM_BACKGROUND_COLOR, new Color(0xCCCCCC));
		this.addKey(Theme.KEY_PRESSED_ITEM_BACKGROUND_COLOR, new Color(0x999999));
		this.addKey(Theme.KEY_ITEM_BORDER_COLOR, new Color(0xCCCCCC));
		this.addKey(Theme.KEY_HOVERED_ITEM_BORDER_COLOR, new Color(0x7A7A7A));
		this.addKey(Theme.KEY_PRESSED_ITEM_BORDER_COLOR, new Color(0x999999));
		this.addKey(Theme.KEY_PRIMARY, new Color(0x00AA00));
		this.addKey(Theme.KEY_BACKGROUND_COLOR, new Color(0xEEEEEE));
	}
}

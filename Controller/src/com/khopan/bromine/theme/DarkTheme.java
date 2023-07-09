package com.khopan.bromine.theme;

import java.awt.Color;

public class DarkTheme extends Theme {
	public static final DarkTheme THEME = new DarkTheme();

	private DarkTheme() {
		this.addKey(Theme.KEY_TEXT_COLOR, new Color(0xFFFFFF));
		this.addKey(Theme.KEY_HOVERED_TEXT_COLOR, new Color(0xFFFFFF));
		this.addKey(Theme.KEY_PRESSED_TEXT_COLOR, new Color(0xFFFFFF));
		this.addKey(Theme.KEY_ITEM_BACKGROUND_COLOR, new Color(0x333333));
		this.addKey(Theme.KEY_HOVERED_ITEM_BACKGROUND_COLOR, new Color(0x333333));
		this.addKey(Theme.KEY_PRESSED_ITEM_BACKGROUND_COLOR, new Color(0x666666));
		this.addKey(Theme.KEY_ITEM_BORDER_COLOR, new Color(0x333333));
		this.addKey(Theme.KEY_HOVERED_ITEM_BORDER_COLOR, new Color(0x858585));
		this.addKey(Theme.KEY_PRESSED_ITEM_BORDER_COLOR, new Color(0x666666));
		this.addKey(Theme.KEY_PRIMARY, new Color(0x00AA00));
		this.addKey(Theme.KEY_BACKGROUND_COLOR, new Color(0x1E1E1E));
	}
}

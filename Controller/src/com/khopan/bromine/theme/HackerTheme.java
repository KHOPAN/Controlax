package com.khopan.bromine.theme;

import java.awt.Color;

public class HackerTheme extends Theme {
	public static final HackerTheme THEME = new HackerTheme();

	private HackerTheme() {
		this.addKey(Theme.KEY_TEXT_COLOR, new Color(0x00FF00));
		this.addKey(Theme.KEY_HOVERED_TEXT_COLOR, new Color(0x00FF00));
		this.addKey(Theme.KEY_PRESSED_TEXT_COLOR, new Color(0x000000));
		this.addKey(Theme.KEY_ITEM_BACKGROUND_COLOR, new Color(0x000000));
		this.addKey(Theme.KEY_HOVERED_ITEM_BACKGROUND_COLOR, new Color(0x000000));
		this.addKey(Theme.KEY_PRESSED_ITEM_BACKGROUND_COLOR, new Color(0x00FF00));
		this.addKey(Theme.KEY_ITEM_BORDER_COLOR, new Color(0x101010));
		this.addKey(Theme.KEY_HOVERED_ITEM_BORDER_COLOR, new Color(0x00FF00));
		this.addKey(Theme.KEY_PRESSED_ITEM_BORDER_COLOR, new Color(0x00FF00));
		this.addKey(Theme.KEY_PRIMARY, new Color(0x00FF00));
		this.addKey(Theme.KEY_BACKGROUND_COLOR, new Color(0x000000));
	}
}

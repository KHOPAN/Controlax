package com.khopan.bromine.theme;

import java.awt.Color;

public class SakuraTheme extends Theme {
	public static final SakuraTheme THEME = new SakuraTheme();

	private SakuraTheme() {
		this.addKey(Theme.KEY_TEXT_COLOR, new Color(0xFFFFFF));
		this.addKey(Theme.KEY_HOVERED_TEXT_COLOR, new Color(0xFFFFFF));
		this.addKey(Theme.KEY_PRESSED_TEXT_COLOR, new Color(0xFFC9D9));
		this.addKey(Theme.KEY_ITEM_BACKGROUND_COLOR, new Color(0xD38DA2));
		this.addKey(Theme.KEY_HOVERED_ITEM_BACKGROUND_COLOR, new Color(0xD38DA2));
		this.addKey(Theme.KEY_PRESSED_ITEM_BACKGROUND_COLOR, new Color(0xB56981));
		this.addKey(Theme.KEY_ITEM_BORDER_COLOR, new Color(0xD38DA2));
		this.addKey(Theme.KEY_HOVERED_ITEM_BORDER_COLOR, new Color(0xB56981));
		this.addKey(Theme.KEY_PRESSED_ITEM_BORDER_COLOR, new Color(0xB56981));
		this.addKey(Theme.KEY_PRIMARY, new Color(0xFFE5EC));
		this.addKey(Theme.KEY_BACKGROUND_COLOR, new Color(0xE2B3BF));
	}
}

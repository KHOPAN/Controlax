package com.khopan.bromine.theme;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

public class Theme {
	public static final String KEY_TEXT_COLOR = "foreground";
	public static final String KEY_HOVERED_TEXT_COLOR = "hoveredForeground";
	public static final String KEY_PRESSED_TEXT_COLOR = "pressedForeground";
	public static final String KEY_ITEM_BACKGROUND_COLOR = "itemBackground";
	public static final String KEY_HOVERED_ITEM_BACKGROUND_COLOR = "hoveredItemBackground";
	public static final String KEY_PRESSED_ITEM_BACKGROUND_COLOR = "pressedItemBackground";
	public static final String KEY_ITEM_BORDER_COLOR = "itemBorder";
	public static final String KEY_HOVERED_ITEM_BORDER_COLOR = "hoveredItemBorder";
	public static final String KEY_PRESSED_ITEM_BORDER_COLOR = "pressedItemBorder";
	public static final String KEY_PRIMARY = "primary";
	public static final String KEY_BACKGROUND_COLOR = "background";

	private final Map<String, Color> map;

	public Theme() {
		this.map = new LinkedHashMap<>();
	}

	public Theme addKey(String key, Color color) {
		this.map.put(key, color);
		return this;
	}

	public Color getColor(String key) {
		return this.map.get(key);
	}

	public int getRGB(String key) {
		return this.map.get(key).getRGB();
	}

	public Map<String, Color> map() {
		return this.map;
	}
}

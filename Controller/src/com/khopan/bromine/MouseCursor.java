package com.khopan.bromine;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

public class MouseCursor {
	public static final MouseCursor DEFAULT_CURSOR = new MouseCursor(Cursor.DEFAULT_CURSOR);
	public static final MouseCursor CROSSHAIR_CURSOR = new MouseCursor(Cursor.CROSSHAIR_CURSOR);
	public static final MouseCursor TEXT_CURSOR = new MouseCursor(Cursor.TEXT_CURSOR);
	public static final MouseCursor WAIT_CURSOR = new MouseCursor(Cursor.WAIT_CURSOR);
	public static final MouseCursor SOUTH_WEST_RESIZE_CURSOR = new MouseCursor(Cursor.SW_RESIZE_CURSOR);
	public static final MouseCursor SOUTH_EAST_RESIZE_CURSOR = new MouseCursor(Cursor.SE_RESIZE_CURSOR);
	public static final MouseCursor NORTH_WEST_RESIZE_CURSOR = new MouseCursor(Cursor.NW_RESIZE_CURSOR);
	public static final MouseCursor NORTH_EAST_RESIZE_CURSOR = new MouseCursor(Cursor.NE_RESIZE_CURSOR);
	public static final MouseCursor NORTH_RESIZE_CURSOR = new MouseCursor(Cursor.N_RESIZE_CURSOR);
	public static final MouseCursor SOUTH_RESIZE_CURSOR = new MouseCursor(Cursor.S_RESIZE_CURSOR);
	public static final MouseCursor WEST_RESIZE_CURSOR = new MouseCursor(Cursor.W_RESIZE_CURSOR);
	public static final MouseCursor EAST_RESIZE_CURSOR = new MouseCursor(Cursor.E_RESIZE_CURSOR);
	public static final MouseCursor HAND_CURSOR = new MouseCursor(Cursor.HAND_CURSOR);
	public static final MouseCursor MOVE_CURSOR = new MouseCursor(Cursor.MOVE_CURSOR);

	private static int Count;

	final Cursor cursor;

	private MouseCursor(int type) {
		this.cursor = new Cursor(type);
	}

	public MouseCursor(Image image, Point hotSpot, String name) {
		this.cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, hotSpot, name);
	}

	public MouseCursor(Image image, Point hotSpot) {
		this(image, hotSpot, MouseCursor.getName());
	}

	public MouseCursor(Image image, String name) {
		this(image, new Point(0, 0), name);
	}

	public MouseCursor(Image image) {
		this(image, new Point(0, 0), MouseCursor.getName());
	}

	private static String getName() {
		MouseCursor.Count++;
		return "Cursor_No_Name_#" + Integer.toString(MouseCursor.Count);
	}
}

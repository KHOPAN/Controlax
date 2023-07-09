package com.khopan.bromine.layout;

import java.awt.Dimension;
import java.awt.Rectangle;

import com.khopan.bromine.Item;
import com.khopan.bromine.RootItem;

public class CenterLayout extends Layout {
	public static final CenterLayout INSTANCE = new CenterLayout();

	private CenterLayout() {

	}

	@Override
	public int getMaxItem() {
		return 1;
	}

	@Override
	public void layoutItemByItem(int index, int count, Item<?> item, RootItem<?> rootItem) {
		Rectangle bounds = item.bounds().get();
		Dimension rootSize = rootItem.size().get();
		bounds.x = (int) Math.round((((double) rootSize.width) - ((double) bounds.width)) * 0.5d);
		bounds.y = (int) Math.round((((double) rootSize.height) - ((double) bounds.height)) * 0.5d);
		item.bounds().set(bounds);
	}
}

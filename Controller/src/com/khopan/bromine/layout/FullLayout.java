package com.khopan.bromine.layout;

import java.awt.Dimension;
import java.awt.Rectangle;

import com.khopan.bromine.Item;
import com.khopan.bromine.RootItem;

public class FullLayout extends Layout {
	public static final FullLayout INSTANCE = new FullLayout(0);

	private final int spacing;

	public FullLayout(int spacing) {
		this.spacing = spacing;
	}

	@Override
	public int getMaxItem() {
		return 1;
	}

	@Override
	public void layoutItemByItem(int index, int count, Item<?> item, RootItem<?> rootItem) {
		Rectangle bounds = item.bounds().get();
		bounds.x = this.spacing;
		bounds.y = this.spacing;
		Dimension size = rootItem.size().get();
		bounds.width = size.width - this.spacing * 2;
		bounds.height = size.height - this.spacing * 2;
		item.bounds().set(bounds);
	}
}

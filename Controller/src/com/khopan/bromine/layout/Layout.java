package com.khopan.bromine.layout;

import java.util.List;

import com.khopan.bromine.Item;
import com.khopan.bromine.RootItem;

public abstract class Layout {
	public static final int MAX_ITEM_INFINITY = -1;

	public static final int LAYOUT_METHOD_ITEM_BY_ITEM = 1;
	public static final int LAYOUT_METHOD_WHOLE_ROOT_ITEM = 2;

	public abstract int getMaxItem();

	public void layoutItemByItem(int index, int count, Item<?> item, RootItem<?> rootItem) {

	}

	public void layoutWholeRootItem(RootItem<?> rootItem, List<Item<?>> itemList) {

	}

	public int getLayoutMethod() {
		return Layout.LAYOUT_METHOD_ITEM_BY_ITEM;
	}
}

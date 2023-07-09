package com.khopan.bromine.layout;

public class AbsoluteLayout extends Layout {
	public static final AbsoluteLayout INSTANCE = new AbsoluteLayout();

	private AbsoluteLayout() {

	}

	@Override
	public int getMaxItem() {
		return Layout.MAX_ITEM_INFINITY;
	}
}

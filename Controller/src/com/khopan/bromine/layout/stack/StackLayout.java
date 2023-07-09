package com.khopan.bromine.layout.stack;

import java.awt.Rectangle;

import com.khopan.bromine.Item;
import com.khopan.bromine.RootItem;
import com.khopan.bromine.layout.Layout;

public class StackLayout extends Layout {
	private final StackDirection direction;

	public StackLayout(StackDirection direction) {
		this.direction = direction == null ? StackDirection.LEFT : direction;
	}

	@Override
	public int getMaxItem() {
		return Layout.MAX_ITEM_INFINITY;
	}

	private int total;

	@Override
	public void layoutItemByItem(int index, int itemSize, Item<?> item, RootItem<?> parent) {
		if(index == 0) {
			this.total = 0;
		}

		Rectangle bounds = item.bounds().get();
		Rectangle parentBounds = parent.bounds().get();

		if(StackDirection.TOP.equals(this.direction)) {
			bounds.x = 0;
			bounds.y = this.total;
			bounds.width = parentBounds.width;
			this.total += bounds.height;
		} else if(StackDirection.LEFT.equals(this.direction)) {
			bounds.x = this.total;
			bounds.y = 0;
			bounds.height = parentBounds.height;
			this.total += bounds.width;
		} else if(StackDirection.BOTTOM.equals(this.direction)) {
			if(index == 0) {
				this.total = parentBounds.height - bounds.height;
			}

			bounds.x = 0;
			bounds.y = this.total;
			bounds.width = parentBounds.width;
			this.total -= bounds.height;
		} else if(StackDirection.RIGHT.equals(this.direction)) {
			if(index == 0) {
				this.total = parentBounds.width - bounds.width;
			}

			bounds.x = this.total;
			bounds.y = 0;
			bounds.height = parentBounds.height;
			this.total -= bounds.width;
		}

		item.bounds().set(bounds);
	}
}

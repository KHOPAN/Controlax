package com.khopan.bromine.layout;

import java.awt.Rectangle;

import com.khopan.bromine.Item;
import com.khopan.bromine.RootItem;

public class MenuLayout extends Layout {
	private final int barSize;

	public MenuLayout(int barSize) {
		this.barSize = barSize;
	}

	@Override
	public int getMaxItem() {
		return 3;
	}

	@Override
	public void layoutItemByItem(int index, int count, Item<?> item, RootItem<?> rootItem) {
		if(count == 1) {
			if(index == 0) {
				Rectangle bounds = item.bounds().get();
				bounds.x = 0;
				bounds.y = 0;
				bounds.setSize(rootItem.size().get());
				item.bounds().set(bounds);
			}
		} else if(count == 2) {
			if(index == 0) {
				item.bounds().set(new Rectangle(0, 0, rootItem.width().get(), this.barSize));
			} else if(index == 1) {
				Rectangle bounds = item.bounds().get();
				bounds.x = 0;
				bounds.y = this.barSize;
				bounds.setSize(rootItem.size().get());
				bounds.height -= this.barSize;
				item.bounds().set(bounds);
			}
		} else if(count == 3) {
			if(index == 0) {
				item.bounds().set(new Rectangle(0, 0, rootItem.width().get(), this.barSize));
			} else if(index == 1) {
				Rectangle bounds = item.bounds().get();
				bounds.x = 0;
				bounds.y = this.barSize;
				bounds.setSize(rootItem.size().get());
				bounds.height -= this.barSize * 2;
				item.bounds().set(bounds);
			} else if(index == 2) {
				Rectangle bounds = item.bounds().get();
				bounds.x = 0;
				bounds.setSize(rootItem.size().get());
				bounds.y = bounds.height - this.barSize;
				bounds.height = this.barSize;
				item.bounds().set(bounds);
			}
		}
	}
}

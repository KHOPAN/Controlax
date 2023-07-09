package com.khopan.bromine.layout;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;

import com.khopan.bromine.Item;
import com.khopan.bromine.RootItem;

public class GridLayout extends Layout {
	private final int rows;
	private final int columns;
	private final int horizontalGaps;
	private final int verticalGaps;

	public GridLayout(int rows, int columns, int horizontalGaps, int verticalGaps) {
		if((rows == 0) && (columns == 0)) {
			throw new IllegalArgumentException("Rows and colums cannot both be zero");
		}

		this.rows = rows;
		this.columns = columns;
		this.horizontalGaps = horizontalGaps;
		this.verticalGaps = verticalGaps;
	}

	public GridLayout(int rows, int columns) {
		this(rows, columns, 0, 0);
	}

	@Override
	public int getMaxItem() {
		return Layout.MAX_ITEM_INFINITY;
	}

	@Override
	public int getLayoutMethod() {
		return Layout.LAYOUT_METHOD_WHOLE_ROOT_ITEM;
	}

	@Override
	public void layoutWholeRootItem(RootItem<?> rootItem, List<Item<?>> itemList) {
		int count = itemList.size();
		int rows = this.rows;
		int columns = this.columns;
		Dimension size = rootItem.size().get();

		if(count == 0) {
			return;
		}

		if(columns > 0) {
			rows = (count + columns - 1) / columns;
		} else {
			columns = (count + rows - 1) / rows;
		}

		int itemWidth = (size.width - (rows - 1) * this.horizontalGaps) / rows;
		int itemHeight = (size.height - (columns - 1) * this.verticalGaps) / columns;

		for(int yIndex = 0, y = 0; yIndex < columns; yIndex++, y += itemHeight + this.verticalGaps) {
			for(int xIndex = 0, x = 0; xIndex < rows; xIndex++, x += itemWidth + this.horizontalGaps) {
				int index = yIndex * rows + xIndex;

				if(index < count) {
					int width = itemWidth;
					int height = itemHeight;

					if(xIndex == rows - 1) {
						width = size.width - x;
					}

					if(yIndex == columns - 1) {
						height = size.height - y;
					}

					itemList.get(index).bounds().set(new Rectangle(x, y, width, height));
				}
			}
		}
	}
}

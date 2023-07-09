package com.khopan.bromine.item.textfield;

import java.awt.Rectangle;

import com.khopan.bromine.area.Area;

class Selection {
	final TextField field;
	final Rectangle bounds;

	boolean selected;
	int start;
	int end;

	Selection(TextField field) {
		this.field = field;
		this.bounds = new Rectangle();
	}

	void render(Area area) {
		/*area.color(255, 0, 0, 128);
		area.fill();
		area.translate(5, 5);
		area.rect(0, 0, 1, 15);*/
	}
}

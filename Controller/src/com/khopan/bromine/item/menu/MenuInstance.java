package com.khopan.bromine.item.menu;

import com.khopan.bromine.property.Getter;
import com.khopan.bromine.theme.Theme;

class MenuInstance {
	Runnable onUpdate;
	Runnable onClose;
	Getter<Theme> theme;

	MenuInstance() {

	}

	void update() {
		if(this.onUpdate != null) {
			this.onUpdate.run();
		}
	}
}

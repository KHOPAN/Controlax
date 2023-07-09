package com.khopan.bromine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.khopan.bromine.area.Area;
import com.khopan.bromine.input.Keyboard;
import com.khopan.bromine.input.Mouse;
import com.khopan.bromine.layout.AbsoluteLayout;
import com.khopan.bromine.layout.Layout;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;
import com.khopan.bromine.theme.Theme;

public abstract class RootItem<T extends RootItem<T>> extends Item<T> {
	protected final List<Item<?>> itemList;

	protected Layout layout;

	private Item<?> focusedItem;

	public RootItem() {
		this.itemList = new ArrayList<>();
		this.layout = AbsoluteLayout.INSTANCE;
		this.focusedItem = null;
	}

	@SuppressWarnings("unchecked")
	public T add(Item<?> item) {
		if(item == null) {
			throw new NullPointerException("Item cannot be null");
		}

		if(item == this) {
			throw new IllegalArgumentException("Item cannot add itself");
		}

		if(item.parent != null) {
			throw new IllegalStateException("Item already have a parent");
		}

		if(this.layout != null) {
			int maxSize = this.layout.getMaxItem();

			if(maxSize > 0 && this.itemList.size() > maxSize) {
				throw new IllegalStateException("Cannot add more than layout's limitation");
			}
		}

		item.parent = this;
		this.itemList.add(item);
		return (T) this;
	}

	public List<Item<?>> itemList() {
		return Collections.unmodifiableList(this.itemList);
	}

	@SuppressWarnings("unchecked")
	public Property<Layout, T> layout() {
		return new SimpleProperty<Layout, T>(() -> this.layout, layout -> this.layout = layout, (T) this).nullable();
	}

	void requestFocus(Item<?> item) {
		this.focusedItem = item;

		if(!item.focused) {
			item.focused = true;
			item.onFocusGained();
		}

		this.update();
	}

	@Override
	protected void render(Area area) {
		for(int i = 0; i < this.itemList.size(); i++) {
			Item<?> item = this.itemList.get(i);

			if(item.visibility) {
				item.render(area.create(item.bounds.x, item.bounds.y, item.bounds.width, item.bounds.height));
			}
		}
	}

	@Override
	protected void renderTop(Area area) {
		for(int i = 0; i < this.itemList.size(); i++) {
			this.itemList.get(i).renderTop(area);
		}
	}

	protected void layoutItem() {
		if(this.layout != null) {
			int method = this.layout.getLayoutMethod();

			if(method == Layout.LAYOUT_METHOD_ITEM_BY_ITEM) {
				int size = this.itemList.size();

				for(int i = 0; i < size; i++) {
					Item<?> item = this.itemList.get(i);
					this.layout.layoutItemByItem(i, size, item, this);
				}
			} else if(method == Layout.LAYOUT_METHOD_WHOLE_ROOT_ITEM) {
				this.layout.layoutWholeRootItem(this, this.itemList);
			} else {
				throw new IllegalArgumentException("Invalid layout method: " + method);
			}
		}
	}

	@Override
	protected void onResize() {
		this.layoutItem();
	}

	@Override
	protected void onThemeUpdate(Theme theme) {
		for(int i = 0; i < this.itemList.size(); i++) {
			Item<?> item = this.itemList.get(i);
			item.theme = theme;
			item.onThemeUpdate(theme);
		}
	}

	@Override
	protected void mouseAction(Mouse mouse) {
		if(mouse.action == Mouse.ACTION_DRAGGED) {
			for(int i = 0; i < this.itemList.size(); i++) {
				Item<?> item = this.itemList.get(i);

				if(item.bounds.contains(mouse.location)) {
					if(item.entered) {
						item.mouseAction(new Mouse(0, 0, Mouse.ACTION_DRAGGED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), 0, 0, 0, 0.0d));
					} else {
						item.entered = true;
						item.mouseAction(new Mouse(0, 0, Mouse.ACTION_ENTERED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), 0, 0, 0, 0.0d));
					}
				} else {
					if(item.entered) {
						item.entered = false;
						item.mouseAction(new Mouse(0, 0, Mouse.ACTION_EXITED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), 0, 0, 0, 0.0d));
					}
				}
			}
		} else if(mouse.action == Mouse.ACTION_MOVED || mouse.action == Mouse.ACTION_ENTERED || mouse.action == Mouse.ACTION_EXITED) {
			for(int i = 0; i < this.itemList.size(); i++) {
				Item<?> item = this.itemList.get(i);

				if(item.bounds.contains(mouse.location)) {
					if(!item.entered) {
						item.entered = true;
						item.mouseAction(new Mouse(0, 0, Mouse.ACTION_ENTERED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), 0, 0, 0, 0.0d));
					} else {
						item.mouseAction(new Mouse(0, 0, Mouse.ACTION_MOVED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), 0, 0, 0, 0.0d));
					}
				} else {
					if(item.entered) {
						item.entered = false;
						item.mouseAction(new Mouse(0, 0, Mouse.ACTION_EXITED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), 0, 0, 0, 0.0d));
					}
				}
			}
		} else if(mouse.action == Mouse.ACTION_PRESSED) {
			int focused = -1;

			for(int i = 0; i < this.itemList.size(); i++) {
				Item<?> item = this.itemList.get(i);

				if(item.bounds.contains(mouse.location) && item.entered) {
					focused = i;
					item.pressed = true;
					item.mouseAction(new Mouse(mouse.button, mouse.clickCount, Mouse.ACTION_PRESSED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), 0, 0, 0, 0.0d));
					this.update();
				}
			}


			if(focused != -1) {
				Item<?> item = this.itemList.get(focused);
				this.unfocusAll(item);
				item.focused = true;
				item.onFocusGained();
				this.focusedItem = item;
			}

			/*for(int i = 0; i < this.itemList.size(); i++) {
				Item<?> item = this.itemList.get(i);

				if(focused == i) {
					if(!item.focused) {
						this.focusedItem = item;
						item.focused = true;
						item.onFocusGained();
					}
				} else {
					if(item.focused) {
						item.focused = false;
						System.out.println("On Focus Lost " + item.getClass());
						item.onFocusLost();
					}
				}
			}*/
		} else if(mouse.action == Mouse.ACTION_RELEASED) {
			for(int i = 0; i < this.itemList.size(); i++) {
				Item<?> item = this.itemList.get(i);

				if(item.pressed) {
					item.pressed = false;
					item.mouseAction(new Mouse(mouse.button, mouse.clickCount, Mouse.ACTION_RELEASED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), 0, 0, 0, 0.0d));
				}

				if(item.bounds.contains(mouse.location) && item.entered) {
					item.mouseAction(new Mouse(mouse.button, mouse.clickCount, Mouse.ACTION_CLICKED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), 0, 0, 0, 0.0d));
				}
			}
		} else if(mouse.action == Mouse.ACTION_WHEEL_MOVED) {
			for(int i = 0; i < this.itemList.size(); i++) {
				Item<?> item = this.itemList.get(i);

				if(item.bounds.contains(mouse.location) && item.entered) {
					item.mouseAction(new Mouse(0, 0, Mouse.ACTION_WHEEL_MOVED, mouse.screenLocation, mouse.modifiers, mouse.extendedModifiers, new Point(mouse.x - item.bounds.x, mouse.y - item.bounds.y), System.currentTimeMillis(), mouse.scrollType, mouse.scrollAmount, mouse.wheelRotation, mouse.preciseWheelRotation));
				}
			}
		}
	}

	void unfocusAll(Item<?> exclude) {
		if(this.parent != null) {
			this.parent.unfocusAll(exclude);
		}
	}

	@Override
	void unfocus(Item<?> exclude) {
		super.unfocus(exclude);

		for(int i = 0; i < this.itemList.size(); i++) {
			Item<?> item = this.itemList.get(i);

			if(item != exclude) {
				item.unfocus(exclude);
			}
		}
	}

	@Override
	protected boolean rootMouseAction(Mouse mouse) {
		for(int i = 0; i < this.itemList.size(); i++) {
			if(this.itemList.get(i).rootMouseAction(mouse)) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected void keyboardAction(Keyboard keyboard) {
		if(this.focusedItem != null) {
			this.focusedItem.keyboardAction(keyboard);
		}
	}
}

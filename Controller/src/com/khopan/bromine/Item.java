package com.khopan.bromine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.khopan.bromine.area.Area;
import com.khopan.bromine.input.Keyboard;
import com.khopan.bromine.input.Mouse;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;
import com.khopan.bromine.property.state.SimpleState;
import com.khopan.bromine.property.state.State;
import com.khopan.bromine.theme.LightTheme;
import com.khopan.bromine.theme.Theme;

public abstract class Item<T extends Item<T>> {
	boolean entered;
	boolean pressed;
	boolean focused;

	protected final Rectangle bounds;

	protected RootItem<?> parent;
	protected Theme theme;
	protected boolean visibility;

	public Item() {
		this.theme = LightTheme.THEME;
		this.bounds = new Rectangle();
		this.visibility = true;
	}

	@SuppressWarnings("unchecked")
	public Property<Integer, T> x() {
		return new SimpleProperty<Integer, T>(() -> this.bounds.x, x -> this.bounds.x = x, x -> this.onBoundsUpdate(this.bounds), (T) this).nullable().whenNull(0);
	}

	@SuppressWarnings("unchecked")
	public Property<Integer, T> y() {
		return new SimpleProperty<Integer, T>(() -> this.bounds.y, y -> this.bounds.y = y, y -> this.onBoundsUpdate(this.bounds), (T) this).nullable().whenNull(0);
	}

	@SuppressWarnings("unchecked")
	public Property<Integer, T> width() {
		return new SimpleProperty<Integer, T>(() -> this.bounds.width, width -> this.bounds.width = width, width -> this.onBoundsUpdate(this.bounds), (T) this).nullable().whenNull(0);
	}

	@SuppressWarnings("unchecked")
	public Property<Integer, T> height() {
		return new SimpleProperty<Integer, T>(() -> this.bounds.height, height -> this.bounds.height = height, height -> this.onBoundsUpdate(this.bounds), (T) this).nullable().whenNull(0);
	}

	@SuppressWarnings("unchecked")
	public Property<Point, T> location() {
		return new SimpleProperty<Point, T>(() -> new Point(this.bounds.x, this.bounds.y), location -> {
			this.bounds.x = location.x;
			this.bounds.y = location.y;
		}, location -> this.onBoundsUpdate(this.bounds), (T) this).nullable().whenNull(new Point());
	}

	@SuppressWarnings("unchecked")
	public Property<Dimension, T> size() {
		return new SimpleProperty<Dimension, T>(() -> new Dimension(this.bounds.width, this.bounds.height), size -> {
			this.bounds.width = size.width;
			this.bounds.height = size.height;
		}, size -> this.onBoundsUpdate(this.bounds), (T) this).nullable().whenNull(new Dimension());
	}

	@SuppressWarnings("unchecked")
	public Property<Rectangle, T> bounds() {
		return new SimpleProperty<Rectangle, T>(() -> this.bounds, bounds -> {
			this.bounds.x = bounds.x;
			this.bounds.y = bounds.y;
			this.bounds.width = bounds.width;
			this.bounds.height = bounds.height;
		}, this :: onBoundsUpdate, (T) this).nullable().whenNull(new Rectangle());
	}

	@SuppressWarnings("unchecked")
	public State<T> visibility() {
		return new SimpleState<T>(() -> this.visibility, visibility -> this.visibility = visibility, this :: onVisibilityUpdate, (T) this);
	}

	@SuppressWarnings("unchecked")
	public Property<Theme, T> theme() {
		return new SimpleProperty<Theme, T>(() -> this.theme, theme -> this.theme = theme, this :: onThemeUpdate, (T) this).nullable().whenNull(LightTheme.THEME);
	}

	@SuppressWarnings("unchecked")
	public Property<MouseCursor, T> cursor() {
		if(this.parent == null) {
			throw new IllegalStateException("Cannot get the cursor's property object because the parent is null");
		}

		Property<MouseCursor, ?> property = this.parent.cursor();
		return new SimpleProperty<MouseCursor, T>(() -> property.get(), cursor -> property.set(cursor), (T) this).nullable().whenNull(MouseCursor.DEFAULT_CURSOR);
	}

	public void center() {
		this.center(Bromine.MONITOR_SIZE);
	}

	public void center(Rectangle bounds) {
		this.center(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public void center(int x, int y, int width, int height) {
		this.bounds.x = (int) Math.round(((double) x) + (((double) width) - ((double) this.bounds.width)) * 0.5d);
		this.bounds.y = (int) Math.round(((double) y) + (((double) height) - ((double) this.bounds.height)) * 0.5d);
		this.onBoundsUpdate(this.bounds);
	}

	public void center(Dimension size) {
		this.center(size.width, size.height);
	}

	public void center(int width, int height) {
		this.bounds.x = (int) Math.round((((double) width) - ((double) this.bounds.width)) * 0.5d);
		this.bounds.y = (int) Math.round((((double) height) - ((double) this.bounds.height)) * 0.5d);
		this.onBoundsUpdate(this.bounds);
	}

	public void update() {
		if(this.parent != null && this.visibility) {
			this.parent.update();
		}
	}

	public void focus() {
		if(this.parent != null) {
			this.parent.requestFocus(this);
		}
	}

	protected void onVisibilityUpdate(boolean visible) {

	}

	protected void onBoundsUpdate(Rectangle bounds) {
		this.onResize();
	}

	protected void onResize() {

	}

	protected void render(Area area) {

	}

	protected void renderTop(Area area) {

	}

	protected void keyboardAction(Keyboard keyboard) {

	}

	protected void mouseAction(Mouse mouse) {

	}

	protected boolean rootMouseAction(Mouse mouse) {
		return false;
	}

	protected void onThemeUpdate(Theme theme) {

	}

	protected void onFocusGained() {

	}

	protected void onFocusLost() {

	}

	public void renderExternal(Area area) {
		this.render(area);
		this.renderTop(area);
	}

	void unfocus(Item<?> exclude) {
		this.focused = false;
		this.onFocusLost();
	}
}

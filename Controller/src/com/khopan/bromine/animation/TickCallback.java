package com.khopan.bromine.animation;

@FunctionalInterface
public interface TickCallback<T> {
	public void tick(T value);
}

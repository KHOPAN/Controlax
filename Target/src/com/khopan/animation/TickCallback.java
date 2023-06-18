package com.khopan.animation;

@FunctionalInterface
public interface TickCallback<T> {
	public void tick(T value);
}

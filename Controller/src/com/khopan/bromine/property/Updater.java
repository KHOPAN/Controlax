package com.khopan.bromine.property;

@FunctionalInterface
public interface Updater<T> {
	public void valueUpdated(T value);
}

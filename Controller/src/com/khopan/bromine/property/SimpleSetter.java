package com.khopan.bromine.property;

@FunctionalInterface
public interface SimpleSetter<T> {
	public void set(T value);
}

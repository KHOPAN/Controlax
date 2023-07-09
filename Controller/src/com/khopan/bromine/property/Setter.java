package com.khopan.bromine.property;

@FunctionalInterface
public interface Setter<T, R> {
	public R set(T value);
}

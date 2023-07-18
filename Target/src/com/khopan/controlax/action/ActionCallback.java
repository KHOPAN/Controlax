package com.khopan.controlax.action;

@FunctionalInterface
public interface ActionCallback<T extends Action> {
	public void callback(T response);
}

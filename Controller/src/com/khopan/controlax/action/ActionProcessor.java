package com.khopan.controlax.action;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

import com.khopan.controlax.Controlax;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class ActionProcessor {
	private Map<Class<? extends Action>, ActionCallback<? extends Action>> classList;

	public ActionProcessor() {
		this.classList = new LinkedHashMap<>();
	}

	public <T extends Action> void register(Class<T> actionClass) {
		this.attach(actionClass, null);
	}

	public <T extends Action> void attach(Class<T> actionClass, ActionCallback<T> action) {
		if(actionClass == null) {
			throw new IllegalArgumentException("Action class cannot be null");
		}

		this.classList.put(actionClass, action);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void receiveAction(BinaryConfigObject config) {
		if(config == null) {
			throw new NullPointerException("Config cannot be null");
		}

		try {
			String name = config.getString("Name");
			Class<?> searchClass = Class.forName(name);
			ActionCallback callback = this.classList.get(searchClass);

			if(callback != null) {
				try {
					Constructor<?> constructor = searchClass.getDeclaredConstructor(BinaryConfigObject.class);
					constructor.setAccessible(true);
					callback.callback((Action) constructor.newInstance(config));
				} catch(NoSuchMethodException Exception) {

				}
			}
		} catch(Throwable Errors) {
			throw new InternalError("Error while process action", Errors);
		}
	}

	public void sendAction(Action action) {
		if(action == null) {
			throw new NullPointerException("Action cannot be null");
		}

		Class<? extends Action> actionClass = action.getClass();
		Controlax.INSTANCE.window.logDebug("Action: " + actionClass.getSimpleName());
		BinaryConfigObject config = new BinaryConfigObject();
		config.putString("Name", actionClass.getName());
		action.actionData(config);
		Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
	}
}

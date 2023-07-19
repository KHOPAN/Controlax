package com.khopan.controlax.processor;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.action.action.MessageAction;
import com.khopan.controlax.action.action.ResponseAction;
import com.khopan.controlax.ui.message.MessageRenderer;

public class MessageProcessor {
	public static void process(MessageAction action) {
		Controlax.INSTANCE.processor.sendAction(ResponseAction.getInstance("Client: Message Received"));
		MessageRenderer.showMessage(action.getMessage());
	}
}

package com.khopan.controlax;

import com.khopan.controlax.ui.message.MessageRenderer;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class MessageProcessor {
	public static void processMessage(BinaryConfigObject config) {
		String message = config.getString("Message");
		BinaryConfigObject response = new BinaryConfigObject();
		response.putInt("Action", 5);
		Controlax.INSTANCE.processor.sendPacket(new BinaryConfigPacket(response));
		MessageRenderer.showMessage(message);
	}
}

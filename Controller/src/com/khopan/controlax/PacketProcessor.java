package com.khopan.controlax;

import java.awt.Dimension;

import com.khopan.lazel.Packet;
import com.khopan.lazel.config.BinaryConfigObject;

public class PacketProcessor {
	public PacketProcessor(Packet packet) {
		if(!packet.isRawData()) {
			BinaryConfigObject config = packet.getBinaryConfigObject();
			int action = config.getInt("Action");

			if(action == 0) {
				ControlaxServer.INSTANCE.screenDimension = new Dimension();
				ControlaxServer.INSTANCE.screenDimension.width = config.getInt("ScreenWidth");
				ControlaxServer.INSTANCE.screenDimension.height = config.getInt("ScreenHeight");
			} else if(action == 1) {
				ControlaxServer.INSTANCE.controlWindow.commandPanel.processCommand(config);
			} else if(action == 2) {
				ControlaxServer.INSTANCE.controlWindow.imagePanel.screenshotPanel.screenshotStatusPane.setText(config.getString("ErrorMessage"));
			}
		} else {
			ControlaxServer.INSTANCE.controlWindow.imagePanel.screenshotPanel.processImagePacket(packet);
		}
	}

	public static void processPacket(Packet packet) {
		new PacketProcessor(packet);
	}
}

package com.khopan.controlax;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.net.InetAddress;

import javax.swing.UIManager;

import com.khopan.controlax.packet.HeaderedImagePacket;
import com.khopan.controlax.ui.ControlWindow;
import com.khopan.controlax.ui.image.ScreenshotPanel;
import com.khopan.controlax.ui.image.StreamRenderer;
import com.khopan.lazel.client.Client;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class Controlax {
	public static Controlax INSTANCE;

	public final IPInputWindow addressInput;
	public ControlWindow window;
	public Client client;

	public Controlax() {
		this.addressInput = new IPInputWindow();
	}

	public void addressEntered(InetAddress address) {
		this.client = new Client();
		this.client.port().set(2553);
		this.client.host().set(address.getHostAddress());
		this.client.connectionListener().set(() -> {
			this.window = new ControlWindow();
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 4);
			config.putBoolean("Start", true);
			config.putInt("Framerate", Controlax.getFramerate());
			this.client.sendPacket(new BinaryConfigPacket(config));
		});

		this.client.packetListener().set(packet -> {
			try {
				BinaryConfigPacket config = packet.getPacket(BinaryConfigPacket.class);
				this.processAction(config.getObject());
			} catch(Throwable Errors) {
				HeaderedImagePacket imagePacket = packet.getPacket(HeaderedImagePacket.class);
				byte header = imagePacket.getHeader();

				if(header == ScreenshotPanel.SCREENSHOT_HEADER) {
					this.window.screenshotPanel.processImagePacket(imagePacket);
				} else if(header == StreamRenderer.STREAM_HEADER) {
					this.window.streamRenderer.processPacket(imagePacket);
				}
			}
		});

		this.client.start();
	}

	private void processAction(BinaryConfigObject config) {
		int action = config.getInt("Action");

		if(action == -1) {
			System.exit(0);
		} else if(action == 1) {
			this.window.commandPanel.processCommand(config);
		} else if(action == 2) {
			this.window.status(config.getString("Error"));
		} else if(action == 5) {
			this.window.status("Message Sent");
		}
	}

	public static int getFramerate() {
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		int lowest = Integer.MAX_VALUE;

		for(int i = 0; i < devices.length; i++) {
			lowest = Math.min(lowest, devices[0].getDisplayMode().getRefreshRate());
		}

		return lowest;
	}

	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		Controlax.INSTANCE = new Controlax();
	}
}

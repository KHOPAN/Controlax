package com.khopan.controlax;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.net.InetAddress;

import javax.swing.UIManager;

import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;
import com.khopan.lazel.server.ClientProcessor;
import com.khopan.lazel.server.Server;

public class Controlax {
	public static final InetAddress IP_ADDRESS = Controlax.getHost();
	public static Controlax INSTANCE;

	public final Server server;
	public final Robot robot;
	public ClientProcessor processor;

	public Controlax() {
		ImageProcessor.load();
		StreamProcessor.load();
		this.server = new Server();
		this.server.port().set(2553);
		this.server.address().set(Controlax.IP_ADDRESS);
		this.server.connectionListener().set(processor -> {
			IPViewer.INSTANCE.frame.dispose();
			this.processor = processor;
			this.processor.packetListener().set(packet -> {
				BinaryConfigPacket config = packet.getPacket(BinaryConfigPacket.class);
				this.processAction(config.getObject());
			});
		});

		this.server.start();

		try {
			this.robot = new Robot();
		} catch(Throwable Errors) {
			throw new InternalError("Error while initializing Robot", Errors);
		}

		IPViewer.view();
	}

	private void processAction(BinaryConfigObject config) {
		int action = config.getInt("Action");

		if(action == 1) {
			CommandProcessor.process(config);
		} else if(action == 2) {
			CommandProcessor.processSystem(config);
		} else if(action == 3) {
			ImageProcessor.processScreenshot();
		} else if(action == 4) {
			if(config.getBoolean("Start")) {
				StreamProcessor.INSTANCE.start(config.getInt("Framerate"));
			} else {
				StreamProcessor.INSTANCE.stop();
			}
		} else if(action == 5) {
			MessageProcessor.processMessage(config);
		} else if(action == 6) {
			ColorProcessor.processColor(config);
		}
	}

	private static InetAddress getHost() {
		try {
			return InetAddress.getLocalHost();
		} catch(Throwable Errors) {
			throw new InternalError("Cannot get host IP address", Errors);
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

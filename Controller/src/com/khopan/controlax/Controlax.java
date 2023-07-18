package com.khopan.controlax;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.UIManager;

import com.khopan.controlax.action.ActionProcessor;
import com.khopan.controlax.action.action.ResponseAction;
import com.khopan.controlax.packet.HeaderedImagePacket;
import com.khopan.controlax.ui.ControlWindow;
import com.khopan.controlax.ui.image.ScreenshotPanel;
import com.khopan.controlax.ui.image.StreamRenderer;
import com.khopan.lazel.client.Client;
import com.khopan.lazel.packet.BinaryConfigPacket;
import com.khopan.lazel.packet.Packet;

public class Controlax {
	public static Controlax INSTANCE;

	public final ActionProcessor processor;

	public ControlWindow window;
	public Client client;

	public Controlax() {
		this.processor = new ActionProcessor();
		this.window = new ControlWindow();
		this.processor.attach(ResponseAction.class, this.window :: response);
	}

	public void processPacket(Packet packet) {
		try {
			BinaryConfigPacket config = packet.getPacket(BinaryConfigPacket.class);
			this.processor.receiveAction(config.getObject());
		} catch(Throwable Errors) {
			HeaderedImagePacket imagePacket = packet.getPacket(HeaderedImagePacket.class);
			byte header = imagePacket.getHeader();

			if(header == ScreenshotPanel.SCREENSHOT_HEADER) {
				this.window.screenshotPanel.processImagePacket(imagePacket);
			} else if(header == StreamRenderer.STREAM_HEADER) {
				this.window.streamRenderer.processPacket(imagePacket);
			}
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

	public static List<NetworkEntry> getLANIPAddress() {
		List<NetworkEntry> entryList = new ArrayList<>();

		try {
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "arp -a");
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String output = "";
			String line;

			while((line = reader.readLine()) != null) {
				output += line + "\n";
			}

			Pattern pattern = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+");
			Matcher matcher = pattern.matcher(output);
			List<Thread> threadList = new ArrayList<>();

			while(matcher.find()) {
				String address = matcher.group();

				Thread thread = new Thread(() -> {
					try {
						NetworkEntry entry = new NetworkEntry();
						entry.address = InetAddress.getByName(address);
						entry.hostAddress = entry.address.getHostAddress();
						entryList.add(entry);
					} catch(Throwable Errors) {
						Errors.printStackTrace();
					}
				});

				thread.start();
				threadList.add(thread);
			}

			for(int i = 0; i < threadList.size(); i++) {
				threadList.get(i).join();
			}

			threadList.clear();
			entryList.sort((first, second) -> {
				return first.hostAddress.compareTo(second.hostAddress);
			});
		} catch(Throwable Errors) {
			Errors.printStackTrace();
		}

		return entryList;
	}

	public static class NetworkEntry {
		public String hostAddress;
		public InetAddress address;
	}

	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		Controlax.INSTANCE = new Controlax();
	}
}

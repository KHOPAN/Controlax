package com.khopan.controlax;

import java.awt.Dimension;
import java.net.InetAddress;

import javax.swing.UIManager;

import com.khopan.controlax.ui.ControlWindow;
import com.khopan.controlax.ui.Window;
import com.khopan.lazel.PacketGateway;
import com.khopan.lazel.server.Server;

public class ControlaxServer {
	public static ControlaxServer INSTANCE;

	public InetAddress localHost;
	public Window window;
	public Server server;
	public PacketGateway gateway;
	public ControlWindow controlWindow;
	public Dimension screenDimension;

	private boolean connected;

	public ControlaxServer() {

	}

	public void initialize() throws Throwable {
		this.localHost = InetAddress.getLocalHost();
		this.window = new Window();
		this.server = new Server();
		this.server.port().set(2553);
		this.server.address().set(this.localHost);
		this.server.clientConnectionListener().set(gateway -> {
			this.gateway = gateway;
			this.gateway.packetReceiver().set(PacketProcessor :: processPacket);

			if(!this.connected) {
				this.window.frame.dispose();
				this.controlWindow = new ControlWindow();
				this.connected = true;
			}
		});

		this.server.start();
	}

	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		ControlaxServer.INSTANCE = new ControlaxServer();
		ControlaxServer.INSTANCE.initialize();
	}
}

package com.khopan.controlax;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.khopan.lazel.Packet;
import com.khopan.lazel.PacketGateway;
import com.khopan.lazel.client.Client;
import com.khopan.lazel.config.BinaryConfigObject;

public class ControlaxClient {
	private boolean stop;
	private PacketGateway gateway;

	private final Robot robot;

	public ControlaxClient() {
		try {
			this.robot = new Robot();
		} catch(Throwable Errors) {
			throw new InternalError(Errors);
		}

		JFrame frame = new JFrame();
		frame.setTitle("Enter IP Address");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent Event) {
				if(!ControlaxClient.this.stop) {
					frame.setVisible(true);
				}
			}

			@Override
			public void windowIconified(WindowEvent Event) {
				if(!ControlaxClient.this.stop) {
					frame.setState(Frame.NORMAL);
				}
			}
		});

		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent Event) {
				if(!ControlaxClient.this.stop) {
					frame.setLocationRelativeTo(null);
				}
			}
		});

		frame.setLayout(new GridLayout(2, 1));
		JTextField textField = new JTextField();
		JButton button = new JButton();
		button.setText("Enter");
		button.setFocusable(false);
		button.addActionListener(Event -> {
			try {
				frame.dispose();
				this.stop = true;
				String text = textField.getText();

				if(text.isEmpty()) {
					throw new InternalError();
				}

				InetAddress address = InetAddress.getByName(text);
				this.startClient(address);
			} catch(Throwable Errors) {
				this.stop = false;
				frame.setVisible(true);
				textField.setText("");
			}
		});

		frame.add(textField);
		frame.add(button);
		frame.setSize(200, 100);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private void startClient(InetAddress address) {
		Client client = new Client();
		client.port().set(2553);
		client.host().set(address.getHostAddress());
		client.serverConnectionListener().set(gateway -> {
			this.gateway = gateway;
			this.gateway.packetReceiver().set(this :: processPacket);
		});

		client.start();
	}

	private void processPacket(Packet packet) {
		if(!packet.isRawData()) {
			BinaryConfigObject config = packet.getBinaryConfigObject();
			int action = config.getInt("Action");

			if(action == 1) {
				this.processCommand(config);
			}
		}
	}

	private void processCommand(BinaryConfigObject config) {
		String directory = config.getString("Directory");
		String command = config.getString("Command");
		int identifierCode = config.getInt("IdentifierCode");
		String output;

		try {
			String resultCommand = "";

			if(directory != null && !directory.isEmpty()) {
				resultCommand += "cd " + directory + " && ";
			}

			resultCommand += command;
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", resultCommand);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			output = "";
			String line;

			while(true) {
				line = reader.readLine();

				if(line == null) {
					break;
				}

				output += line + "\n";
			}
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Client: Error while executing command", Errors).printStackTrace(printWriter);
			output = stringWriter.toString();
		}

		BinaryConfigObject result = new BinaryConfigObject();
		result.putInt("Action", 1);
		result.putInt("IdentifierCode", identifierCode);
		result.putString("Result", output);
		this.gateway.sendPacket(new Packet(result));
	}

	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		new ControlaxClient();
	}
}

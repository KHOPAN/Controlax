package com.khopan.controlax;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.khopan.controlax.ui.color.ColorDisplay;
import com.khopan.lazel.Packet;
import com.khopan.lazel.PacketGateway;
import com.khopan.lazel.client.Client;
import com.khopan.lazel.config.BinaryConfigObject;

public class ControlaxClient {
	private final Dimension screenDimension;
	private final Robot robot;

	private boolean stop;
	private PacketGateway gateway;


	public ControlaxClient() {
		this.screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

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
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 0);
			config.putInt("ScreenWidth", this.screenDimension.width);
			config.putInt("ScreenHeight", this.screenDimension.height);
			this.gateway.sendPacket(new Packet(config));
		});

		client.start();
	}

	private void processPacket(Packet packet) {
		if(!packet.isRawData()) {
			BinaryConfigObject config = packet.getBinaryConfigObject();
			int action = config.getInt("Action");

			if(action == 1) {
				this.processCommand(config);
			} else if(action == 2) {
				this.processScreenshot();
			} else if(action == 3) {
				this.processColor(config);
			}
		}
	}

	private void processColor(BinaryConfigObject config) {
		int action = config.getInt("SubAction");

		if(action == 0) {
			ColorDisplay.clear();
		} else if(action == 1) {
			int color = config.getInt("Color");
			float transparency = config.getFloat("Transparency");
			ColorDisplay.transparency(transparency);
			ColorDisplay.display(color);
		} else if(action == 2) {
			boolean moving = config.getBoolean("Moving");
			float transparency = config.getFloat("Transparency");
			float rate = config.getFloat("Rate");
			ColorDisplay.transparency(transparency);
			ColorDisplay.rainbowRate(rate);
			ColorDisplay.rainbow(moving);
		}
	}

	private void processScreenshot() {
		try {
			Rectangle bounds = new Rectangle();
			bounds.x = 0;
			bounds.y = 0;
			bounds.width = this.screenDimension.width;
			bounds.height = this.screenDimension.height;
			BufferedImage screenshot = this.robot.createScreenCapture(bounds);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageIO.write(screenshot, "png", stream);
			byte[] byteArray = stream.toByteArray();
			int length = byteArray.length;
			int packets = 0;

			while(true) {
				if(packets * 65536 >= length) {
					break;
				}

				packets++;
			}

			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putBoolean("Error", false);
			config.putInt("PacketSize", packets);
			this.gateway.sendPacket(new Packet(config));
			ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
			int remaining = length;

			for(int i = 0; i < packets; i++) {
				this.gateway.sendPacket(new Packet(inputStream.readNBytes(remaining > 65536 ? 65536 : remaining)));
				remaining -= 65536;
			}
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Client: Error while taking screenshot", Errors).printStackTrace(printWriter);
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putBoolean("Error", true);
			config.putString("ErrorMessage", stringWriter.toString());
			this.gateway.sendPacket(new Packet(config));
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

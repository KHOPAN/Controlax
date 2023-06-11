package com.khopan.controlax;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.khopan.lazel.Packet;
import com.khopan.lazel.PacketGateway;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.server.Server;

public class ControlaxServer {
	private boolean connected;
	private PacketGateway gateway;
	private JTextPane commandOutputPane;
	private int commandIdentifierCode;

	public ControlaxServer() throws Throwable {
		//this.begin(null);
		InetAddress address = InetAddress.getLocalHost();
		JFrame frame = new JFrame();
		frame.setTitle("IP Address Viewer");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JLabel label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText(address.getHostAddress());
		frame.add(label, BorderLayout.CENTER);
		frame.setSize(200, 100);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		Server server = new Server();
		server.port().set(2553);
		server.address().set(address);
		server.clientConnectionListener().set(gateway -> {
			this.gateway = gateway;
			this.gateway.packetReceiver().set(this :: processPacket);

			if(!this.connected) {
				frame.dispose();
				this.begin(this.gateway);
				this.connected = true;
			}
		});

		server.start();
	}

	private void begin(PacketGateway gateway) {
		JFrame frame = new JFrame();
		frame.setTitle("Control Panel");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setLayout(new BorderLayout());

		panel.add(this.commandPanel(), BorderLayout.CENTER);
		frame.add(panel, BorderLayout.CENTER);
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private JPanel commandPanel() {
		JPanel commandPanel = new JPanel();
		commandPanel.setBorder(new TitledBorder("Command"));
		commandPanel.setLayout(new GridLayout(2, 1));
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(new TitledBorder("Control"));
		controlPanel.setLayout(new GridLayout(5, 1));
		JPanel directoryPanel = new JPanel();
		directoryPanel.setLayout(new GridLayout(1, 2));
		JLabel directoryLabel = new JLabel();
		directoryLabel.setText("Directory:");
		directoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
		directoryPanel.add(directoryLabel);
		JTextField directoryField = new JTextField();
		directoryPanel.add(directoryField);
		controlPanel.add(directoryPanel);
		JPanel inputCommandPanel = new JPanel();
		inputCommandPanel.setLayout(new GridLayout(1, 2));
		JLabel inputCommandLabel = new JLabel();
		inputCommandLabel.setText("Input Command:");
		inputCommandLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputCommandPanel.add(inputCommandLabel);
		JTextField inputCommandField = new JTextField();
		inputCommandPanel.add(inputCommandField);
		controlPanel.add(inputCommandPanel);
		JButton sendCommandButton = new JButton();
		sendCommandButton.setText("Send Command");
		controlPanel.add(sendCommandButton);
		JButton clearOutputButton = new JButton();
		clearOutputButton.setText("Clear Output Window");
		controlPanel.add(clearOutputButton);
		JCheckBox clearOnSendCheckbox = new JCheckBox();
		clearOnSendCheckbox.setText("Clear On Send");
		clearOnSendCheckbox.setSelected(true);
		controlPanel.add(clearOnSendCheckbox);

		commandPanel.add(controlPanel);
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder("Output"));
		outputPanel.setLayout(new BorderLayout());
		this.commandOutputPane = new JTextPane();
		clearOutputButton.addActionListener(Event -> {
			this.commandOutputPane.setText("");
		});

		sendCommandButton.addActionListener(Event -> {
			if(clearOnSendCheckbox.isSelected()) {
				this.commandOutputPane.setText("");
			}

			this.sendCommand(directoryField.getText(), inputCommandField.getText());
		});

		this.commandOutputPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(this.commandOutputPane);
		outputPanel.add(scrollPane, BorderLayout.CENTER);
		commandPanel.add(outputPanel);
		return commandPanel;
	}

	private void sendCommand(String directory, String command) {
		try {
			if(command == null || command.isEmpty()) {
				this.commandOutputPane.setText("Error: Empty Command");
				return;
			}

			this.commandIdentifierCode = ThreadLocalRandom.current().nextInt(0xFFFFFF + 1);
			this.commandOutputPane.setText("Sending Command...\nIdentfiier Code: 0x" + String.format("%06x", this.commandIdentifierCode).toUpperCase());
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 1);
			config.putString("Directory", directory);
			config.putString("Command", command);
			config.putInt("IdentifierCode", this.commandIdentifierCode);
			this.gateway.sendPacket(new Packet(config));
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Error while send command", Errors).printStackTrace(printWriter);
			this.commandOutputPane.setText(stringWriter.toString());
		}
	}

	private void processCommand(BinaryConfigObject config) {
		int identifierCode = config.getInt("IdentifierCode");

		if(this.commandIdentifierCode == identifierCode) {
			this.commandOutputPane.setText(this.commandOutputPane.getText() + "\n" + config.getString("Result"));
		} else {
			this.commandOutputPane.setText(this.commandOutputPane.getText() + "\n" + "Error: Invalid identifier code\nValid Code: " + String.format("%06x", this.commandIdentifierCode).toUpperCase() + "\nReceived Code: " + String.format("%06x", identifierCode).toUpperCase());
		}
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

	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		new ControlaxServer();
	}
}

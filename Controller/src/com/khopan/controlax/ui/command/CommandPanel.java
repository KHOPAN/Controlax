package com.khopan.controlax.ui.command;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class CommandPanel extends JPanel {
	private static final long serialVersionUID = -5085545801193268345L;

	public final JTextPane commandOutputPane;
	public final JTextField directoryField;
	public final JTextField inputCommandField;
	public final JButton sleepButton;
	public final JButton shutdownButton;
	public final JButton restartButton;
	public final JButton sendCommandButton;
	public final JButton clearOutputButton;

	private int commandIdentifierCode;

	public CommandPanel() {
		this.setBorder(new TitledBorder("Command"));
		this.setLayout(new GridLayout(1, 2));
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder("Output"));
		outputPanel.setLayout(new BorderLayout());
		this.commandOutputPane = new JTextPane();
		this.commandOutputPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(this.commandOutputPane);
		outputPanel.add(scrollPane, BorderLayout.CENTER);
		this.add(outputPanel);
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(new TitledBorder("Control"));
		controlPanel.setLayout(new GridLayout(4, 1));
		JPanel directoryPanel = new JPanel();
		directoryPanel.setLayout(new GridLayout(1, 2));
		JLabel directoryLabel = new JLabel();
		directoryLabel.setText("Directory:");
		directoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
		directoryPanel.add(directoryLabel);
		this.directoryField = new JTextField();
		directoryPanel.add(this.directoryField);
		controlPanel.add(directoryPanel);
		JPanel inputCommandPanel = new JPanel();
		inputCommandPanel.setLayout(new GridLayout(1, 2));
		JLabel inputCommandLabel = new JLabel();
		inputCommandLabel.setText("Input Command:");
		inputCommandLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputCommandPanel.add(inputCommandLabel);
		this.inputCommandField = new JTextField();
		inputCommandPanel.add(this.inputCommandField);
		controlPanel.add(inputCommandPanel);
		JPanel systemActionPanel = new JPanel();
		systemActionPanel.setLayout(new GridLayout(1, 3));
		this.sleepButton = new JButton();
		this.sleepButton.setText("Sleep");
		this.sleepButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putInt("SubAction", 1);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		});

		systemActionPanel.add(this.sleepButton);
		this.shutdownButton = new JButton();
		this.shutdownButton.setText("Shutdown");
		this.shutdownButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putInt("SubAction", 2);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		});

		systemActionPanel.add(this.shutdownButton);
		this.restartButton = new JButton();
		this.restartButton.setText("Restart");
		this.restartButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putInt("SubAction", 3);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		});

		systemActionPanel.add(this.restartButton);
		controlPanel.add(systemActionPanel);
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridLayout(1, 2));
		this.sendCommandButton = new JButton();
		this.sendCommandButton.setText("Send Command");
		this.sendCommandButton.addActionListener(Event -> {
			this.commandOutputPane.setText("");
			this.sendCommand();
		});

		actionPanel.add(this.sendCommandButton);
		this.clearOutputButton = new JButton();
		this.clearOutputButton.setText("Clear Output Window");
		this.clearOutputButton.addActionListener(Event -> {
			this.commandOutputPane.setText("");
		});

		actionPanel.add(this.clearOutputButton);
		controlPanel.add(actionPanel);
		this.add(controlPanel);
	}

	private void sendCommand() {
		try {
			String command = this.inputCommandField.getText();
			String directory = this.directoryField.getText();

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
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Error while sending a command", Errors).printStackTrace(printWriter);
			this.commandOutputPane.setText(stringWriter.toString());
		}
	}

	public void processCommand(BinaryConfigObject config) {
		int identifierCode = config.getInt("IdentifierCode");

		if(this.commandIdentifierCode == identifierCode) {
			this.commandOutputPane.setText(config.getString("Result"));
		} else {
			this.commandOutputPane.setText("Error: Invalid identifier code\nValid Code: " + String.format("%06x", this.commandIdentifierCode).toUpperCase() + "\nReceived Code: " + String.format("%06x", identifierCode).toUpperCase());
		}
	}
}

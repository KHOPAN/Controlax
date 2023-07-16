package com.khopan.controlax.ui.command;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class CommandPanel extends JPanel {
	private static final long serialVersionUID = -5085545801193268345L;

	public final JTextField directoryField;
	public final JTextField inputCommandField;
	public final JButton sleepButton;
	public final JButton shutdownButton;
	public final JButton restartButton;
	public final JButton sendCommandButton;
	public final JButton clearOutputButton;
	public final JButton emergencyButton;

	private int commandIdentifierCode;

	public CommandPanel() {
		this.setBorder(new TitledBorder("Command"));
		this.setLayout(new GridLayout(5, 1));
		JPanel directoryPanel = new JPanel();
		directoryPanel.setLayout(new GridLayout(1, 2));
		JLabel directoryLabel = new JLabel();
		directoryLabel.setText("Directory:");
		directoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
		directoryPanel.add(directoryLabel);
		this.directoryField = new JTextField();
		directoryPanel.add(this.directoryField);
		this.add(directoryPanel);
		JPanel inputCommandPanel = new JPanel();
		inputCommandPanel.setLayout(new GridLayout(1, 2));
		JLabel inputCommandLabel = new JLabel();
		inputCommandLabel.setText("Input Command:");
		inputCommandLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputCommandPanel.add(inputCommandLabel);
		this.inputCommandField = new JTextField();
		inputCommandPanel.add(this.inputCommandField);
		this.add(inputCommandPanel);
		JPanel systemActionPanel = new JPanel();
		systemActionPanel.setLayout(new GridLayout(1, 3));
		this.sleepButton = new JButton();
		this.sleepButton.setText("Sleep");
		this.sleepButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putInt("SubAction", 1);
			Controlax.INSTANCE.selected.sendPacket(new BinaryConfigPacket(config));
		});

		systemActionPanel.add(this.sleepButton);
		this.shutdownButton = new JButton();
		this.shutdownButton.setText("Shutdown");
		this.shutdownButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putInt("SubAction", 2);
			Controlax.INSTANCE.selected.sendPacket(new BinaryConfigPacket(config));
		});

		systemActionPanel.add(this.shutdownButton);
		this.restartButton = new JButton();
		this.restartButton.setText("Restart");
		this.restartButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putInt("SubAction", 3);
			Controlax.INSTANCE.selected.sendPacket(new BinaryConfigPacket(config));
		});

		systemActionPanel.add(this.restartButton);
		this.add(systemActionPanel);
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridLayout(1, 2));
		this.sendCommandButton = new JButton();
		this.sendCommandButton.setText("Send Command");
		this.sendCommandButton.addActionListener(Event -> {
			this.sendCommand();
		});

		actionPanel.add(this.sendCommandButton);
		this.clearOutputButton = new JButton();
		this.clearOutputButton.setText("Clear Output Window");
		this.clearOutputButton.addActionListener(Event -> {
			Controlax.INSTANCE.window.statusPane.setText("");
		});

		actionPanel.add(this.clearOutputButton);
		this.add(actionPanel);
		this.emergencyButton = new JButton();
		this.emergencyButton.setText("Emergency Terminate");
		this.emergencyButton.setForeground(new Color(0xFF0000));
		this.emergencyButton.addActionListener(Event -> {
			int response = JOptionPane.showConfirmDialog(Controlax.INSTANCE.window.frame, "Are you sure you want to terminate Controlax on the target side?", "Confirm Termination", JOptionPane.YES_NO_CANCEL_OPTION);

			if(response == JOptionPane.YES_OPTION) {
				BinaryConfigObject config = new BinaryConfigObject();
				config.putInt("Action", -1);
				Controlax.INSTANCE.selected.sendPacket(new BinaryConfigPacket(config));
			}
		});

		this.add(this.emergencyButton);
	}

	private void sendCommand() {
		try {
			String command = this.inputCommandField.getText();
			String directory = this.directoryField.getText();

			if(command == null || command.isEmpty()) {
				Controlax.INSTANCE.window.status("Error: Empty Command");
				return;
			}

			this.commandIdentifierCode = ThreadLocalRandom.current().nextInt(0xFFFFFF + 1);
			Controlax.INSTANCE.window.status("Sending Command...\nIdentfiier Code: 0x" + String.format("%06x", this.commandIdentifierCode).toUpperCase());
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 1);
			config.putString("Directory", directory);
			config.putString("Command", command);
			config.putInt("IdentifierCode", this.commandIdentifierCode);
			Controlax.INSTANCE.selected.sendPacket(new BinaryConfigPacket(config));
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Error while sending a command", Errors).printStackTrace(printWriter);
			Controlax.INSTANCE.window.status(stringWriter.toString());
		}
	}

	public void processCommand(BinaryConfigObject config) {
		int identifierCode = config.getInt("IdentifierCode");

		if(this.commandIdentifierCode == identifierCode) {
			Controlax.INSTANCE.window.status(config.getString("Result"));
		} else {
			Controlax.INSTANCE.window.status("Error: Invalid identifier code\nValid Code: " + String.format("%06x", this.commandIdentifierCode).toUpperCase() + "\nReceived Code: " + String.format("%06x", identifierCode).toUpperCase());
		}
	}
}

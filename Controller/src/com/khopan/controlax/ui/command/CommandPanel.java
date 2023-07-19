package com.khopan.controlax.ui.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.action.action.CommandAction;
import com.khopan.controlax.action.action.EmergencyTerminateAction;
import com.khopan.controlax.action.action.PowerAction;

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

	public CommandPanel(int labelBorder) {
		this.setBorder(new TitledBorder("Command"));
		this.setLayout(new GridLayout(5, 1));
		JPanel directoryPanel = new JPanel();
		directoryPanel.setLayout(new BorderLayout());
		JLabel directoryLabel = new JLabel();
		directoryLabel.setBorder(new EmptyBorder(labelBorder, labelBorder, labelBorder, labelBorder));
		directoryLabel.setText("Directory:");
		directoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
		directoryPanel.add(directoryLabel, BorderLayout.WEST);
		this.directoryField = new JTextField();
		directoryPanel.add(this.directoryField, BorderLayout.CENTER);
		this.add(directoryPanel);
		JPanel inputCommandPanel = new JPanel();
		inputCommandPanel.setLayout(new BorderLayout());
		JLabel inputCommandLabel = new JLabel();
		inputCommandLabel.setBorder(new EmptyBorder(labelBorder, labelBorder, labelBorder, labelBorder));
		inputCommandLabel.setText("Input Command:");
		inputCommandLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputCommandPanel.add(inputCommandLabel, BorderLayout.WEST);
		this.inputCommandField = new JTextField();
		inputCommandPanel.add(this.inputCommandField, BorderLayout.CENTER);
		this.add(inputCommandPanel);
		JPanel systemActionPanel = new JPanel();
		systemActionPanel.setLayout(new GridLayout(1, 3));
		this.sleepButton = new JButton();
		this.sleepButton.setText("Sleep");
		this.sleepButton.addActionListener(Event -> Controlax.INSTANCE.processor.sendAction(PowerAction.getSleep()));
		systemActionPanel.add(this.sleepButton);
		this.shutdownButton = new JButton();
		this.shutdownButton.setText("Shutdown");
		this.shutdownButton.addActionListener(Event -> Controlax.INSTANCE.processor.sendAction(PowerAction.getShutdown()));
		systemActionPanel.add(this.shutdownButton);
		this.restartButton = new JButton();
		this.restartButton.setText("Restart");
		this.restartButton.addActionListener(Event -> Controlax.INSTANCE.processor.sendAction(PowerAction.getRestart()));
		systemActionPanel.add(this.restartButton);
		this.add(systemActionPanel);
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridLayout(1, 2));
		this.sendCommandButton = new JButton();
		this.sendCommandButton.setText("Send Command");
		this.sendCommandButton.addActionListener(Event -> this.sendCommand());
		actionPanel.add(this.sendCommandButton);
		this.clearOutputButton = new JButton();
		this.clearOutputButton.setText("Clear Output Window");
		this.clearOutputButton.addActionListener(Event -> Controlax.INSTANCE.window.statusPane.setText(""));
		actionPanel.add(this.clearOutputButton);
		this.add(actionPanel);
		this.emergencyButton = new JButton();
		this.emergencyButton.setText("Emergency Terminate");
		this.emergencyButton.setForeground(new Color(0xFF0000));
		this.emergencyButton.addActionListener(Event -> this.emergencyTerminate());
		this.add(this.emergencyButton);
	}

	private void sendCommand() {
		try {
			String command = this.inputCommandField.getText();
			String directory = this.directoryField.getText();

			if(command == null || command.isEmpty()) {
				Controlax.INSTANCE.window.logError("Error: Empty Command");
				return;
			}

			Controlax.INSTANCE.window.logNormal("Command: Sent");
			Controlax.INSTANCE.processor.sendAction(CommandAction.getInstance(directory, command));
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Error while sending a command", Errors).printStackTrace(printWriter);
			Controlax.INSTANCE.window.logError(stringWriter.toString());
		}
	}

	private void emergencyTerminate() {
		int response = JOptionPane.showConfirmDialog(Controlax.INSTANCE.window.frame, "Are you sure you want to terminate Controlax on the target side?", "Confirm Termination", JOptionPane.YES_NO_CANCEL_OPTION);

		if(response == JOptionPane.YES_OPTION) {
			Controlax.INSTANCE.processor.sendAction(EmergencyTerminateAction.getInstance());
		}
	}
}

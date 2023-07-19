package com.khopan.controlax.ui.execute;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.packet.FilePacket;

public class ExecutePanel extends JPanel {
	private static final long serialVersionUID = 938684876224469855L;

	public final JTextField pathInputField;
	public final JTextField destinationInputField;
	public final JButton chooseFileButton;
	public final JButton executeButton;

	public ExecutePanel(int labelBorder) {
		this.setBorder(new TitledBorder("Execute"));
		this.setLayout(new GridLayout(4, 1));
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new BorderLayout());
		JLabel pathLabel = new JLabel();
		pathLabel.setBorder(new EmptyBorder(labelBorder, labelBorder, labelBorder, labelBorder));
		pathLabel.setText("Executable Path:");
		pathPanel.add(pathLabel, BorderLayout.WEST);
		this.pathInputField = new JTextField();
		pathPanel.add(this.pathInputField, BorderLayout.CENTER);
		this.add(pathPanel);
		JPanel destinationPanel = new JPanel();
		destinationPanel.setLayout(new BorderLayout());
		JLabel destinationLabel = new JLabel();
		destinationLabel.setBorder(new EmptyBorder(labelBorder, labelBorder, labelBorder, labelBorder));
		destinationLabel.setText("Destination Path:");
		destinationPanel.add(destinationLabel, BorderLayout.WEST);
		this.destinationInputField = new JTextField();
		destinationPanel.add(this.destinationInputField, BorderLayout.CENTER);
		this.add(destinationPanel);

		this.chooseFileButton = new JButton();
		this.chooseFileButton.setText("Choose Executable");
		this.chooseFileButton.addActionListener(Event -> this.chooseFile());
		this.add(this.chooseFileButton);
		this.executeButton = new JButton();
		this.executeButton.setText("Execute");
		this.executeButton.addActionListener(Event -> this.execute());
		this.add(this.executeButton);
	}

	private void chooseFile() {
		FileDialog dialog = new FileDialog((Frame) null);
		dialog.setMultipleMode(false);
		dialog.setVisible(true);
		String filePath = dialog.getFile();

		if(filePath != null) {
			File file = new File(dialog.getDirectory(), filePath);
			this.pathInputField.setText(file.getAbsolutePath());
		}
	}

	private void execute() {
		String rawPath = this.pathInputField.getText();

		if(rawPath == null || rawPath.isEmpty()) {
			Controlax.INSTANCE.window.status("Error: Empty Path");
		}

		String rawDestination = this.destinationInputField.getText();

		if(rawDestination == null || rawDestination.isEmpty()) {
			rawDestination = rawPath;
		}

		File file = new File(rawPath);
		File destination = new File(rawDestination);

		if(file.exists()) {
			Controlax.INSTANCE.client.sendPacket(new FilePacket(file, destination));
		} else {
			Controlax.INSTANCE.window.status("Error: File Not Exist");
		}
	}
}

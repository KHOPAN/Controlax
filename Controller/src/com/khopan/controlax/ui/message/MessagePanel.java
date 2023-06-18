package com.khopan.controlax.ui.message;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class MessagePanel extends JPanel {
	private static final long serialVersionUID = -1146305489305456275L;

	public final JTextPane messageOutputPane;
	public final JTextPane messageInputPane;
	public final JButton sendMessageButton;
	public final JButton clearOutputButton;

	public MessagePanel() {
		this.setBorder(new TitledBorder("Message"));
		this.setLayout(new GridLayout(1, 2));
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder("Output"));
		outputPanel.setLayout(new BorderLayout());
		this.messageOutputPane = new JTextPane();
		this.messageOutputPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(this.messageOutputPane);
		outputPanel.add(scrollPane, BorderLayout.CENTER);
		this.add(outputPanel);
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(new TitledBorder("Control"));
		controlPanel.setLayout(new GridLayout(2, 1));
		JPanel messageInputPanel = new JPanel();
		messageInputPanel.setLayout(new GridLayout(1, 2));
		JLabel messageInputLabel = new JLabel();
		messageInputLabel.setText("Message:");
		messageInputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageInputPanel.add(messageInputLabel);
		this.messageInputPane = new JTextPane();
		JScrollPane messageInputScrollPane = new JScrollPane(this.messageInputPane);
		messageInputPanel.add(messageInputScrollPane);
		controlPanel.add(messageInputPanel);
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridLayout(2, 1));
		this.sendMessageButton = new JButton();
		this.sendMessageButton.setText("Send Message");
		this.sendMessageButton.addActionListener(Event -> this.sendMessage());
		actionPanel.add(this.sendMessageButton);
		this.clearOutputButton = new JButton();
		this.clearOutputButton.setText("Clear Output Window");
		this.clearOutputButton.addActionListener(Event -> this.messageOutputPane.setText(""));
		actionPanel.add(this.clearOutputButton);
		controlPanel.add(actionPanel);
		this.add(controlPanel);
	}

	private void sendMessage() {
		String message = this.messageInputPane.getText();

		if(message == null || message.isEmpty()) {
			this.messageOutputPane.setText("Error: Empty Message");
			return;
		}

		this.messageOutputPane.setText("Sending a Message...");
		BinaryConfigObject config = new BinaryConfigObject();
		config.putInt("Action", 5);
		config.putString("Message", message);
		Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
	}
}

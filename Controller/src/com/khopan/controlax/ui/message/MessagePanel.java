package com.khopan.controlax.ui.message;

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

	public final JTextPane messageInputPane;
	public final JButton sendMessageButton;
	public final JButton testTargetButton;
	public final JButton chatButton;

	public MessagePanel() {
		this.setBorder(new TitledBorder("Message"));
		this.setLayout(new GridLayout(2, 1));
		JPanel messageInputPanel = new JPanel();
		messageInputPanel.setLayout(new GridLayout(1, 2));
		JLabel messageInputLabel = new JLabel();
		messageInputLabel.setText("Message:");
		messageInputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageInputPanel.add(messageInputLabel);
		this.messageInputPane = new JTextPane();
		JScrollPane messageInputScrollPane = new JScrollPane(this.messageInputPane);
		messageInputPanel.add(messageInputScrollPane);
		this.add(messageInputPanel);
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridLayout(2, 1));
		this.sendMessageButton = new JButton();
		this.sendMessageButton.setText("Send Message");
		this.sendMessageButton.addActionListener(Event -> this.sendMessage());
		actionPanel.add(this.sendMessageButton);
		this.testTargetButton = new JButton();
		this.testTargetButton.setText("Test Target");
		this.testTargetButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 4); // REUSED CODE NUMBER
			Controlax.INSTANCE.selected.sendPacket(new BinaryConfigPacket(config));
		});

		actionPanel.add(this.testTargetButton);
		this.chatButton = new JButton();
		this.chatButton.setText("Open Chat");
		this.chatButton.addActionListener(Event -> Controlax.INSTANCE.window.chatWindow.frame.setVisible(true));
		//actionPanel.add(this.chatButton);
		this.add(actionPanel);
	}

	private void sendMessage() {
		String message = this.messageInputPane.getText();

		if(message == null || message.isEmpty()) {
			Controlax.INSTANCE.window.status("Error: Empty Message");
			return;
		}

		Controlax.INSTANCE.window.status("Sending a Message...");
		BinaryConfigObject config = new BinaryConfigObject();
		config.putInt("Action", 5);
		config.putString("Message", message);
		Controlax.INSTANCE.selected.sendPacket(new BinaryConfigPacket(config));
	}
}

package com.khopan.controlax.ui.image;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class StreamPanel extends JPanel {
	private static final long serialVersionUID = -7327805975937257834L;

	public static final byte STREAM_HEADER = 0x5F;

	public final JTextPane streamStatusPane;
	public final JButton startStreamButton;
	public final JButton stopStreamButton;

	public BufferedImage image;

	public StreamPanel() {
		this.setBorder(new TitledBorder("Stream"));
		this.setLayout(new GridLayout(1, 2));
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new TitledBorder("Status"));
		statusPanel.setLayout(new BorderLayout());
		this.streamStatusPane = new JTextPane();
		this.streamStatusPane.setEditable(false);
		JScrollPane statusScrollPane = new JScrollPane(this.streamStatusPane);
		statusPanel.add(statusScrollPane, BorderLayout.CENTER);
		this.add(statusPanel);
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(2, 1));
		this.startStreamButton = new JButton();
		this.startStreamButton.setText("Start Streaming");
		this.startStreamButton.addActionListener(Event -> this.start());
		controlPanel.add(this.startStreamButton);
		this.stopStreamButton = new JButton();
		this.stopStreamButton.setText("Stop Streaming");
		this.stopStreamButton.addActionListener(Event -> this.stop());
		controlPanel.add(this.stopStreamButton);
		this.add(controlPanel);
	}

	private void start() {
		this.streamStatusPane.setText("Stream started");
		BinaryConfigObject config = new BinaryConfigObject();
		config.putInt("Action", 4);
		config.putBoolean("Start", true);
		config.putInt("Framerate", Controlax.getFramerate());
		Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		StreamRenderer.INSTANCE.start();
	}

	private void stop() {
		this.streamStatusPane.setText("Stream stopped");
		BinaryConfigObject config = new BinaryConfigObject();
		config.putInt("Action", 4);
		config.putBoolean("Start", false);
		Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		StreamRenderer.INSTANCE.stop();
	}
}

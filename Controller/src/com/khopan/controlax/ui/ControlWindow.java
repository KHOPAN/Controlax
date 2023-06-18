package com.khopan.controlax.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.khopan.controlax.ui.color.ColorPanel;
import com.khopan.controlax.ui.command.CommandPanel;
import com.khopan.controlax.ui.image.ImagePanel;
import com.khopan.controlax.ui.message.MessagePanel;

public class ControlWindow {
	public final JFrame frame;
	public final ColorPanel colorPanel;
	public final CommandPanel commandPanel;
	public final MessagePanel messagePanel;
	public final ImagePanel imagePanel;

	public ControlWindow() {
		this.frame = new JFrame();
		this.frame.setTitle("Control Panel");
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		panel.setLayout(new GridLayout(1, 2));
		JPanel firstSubPanel = new JPanel();
		firstSubPanel.setLayout(new GridLayout(2, 1));
		this.colorPanel = new ColorPanel();
		firstSubPanel.add(this.colorPanel);
		JLabel unimplementedLabel = new JLabel();
		unimplementedLabel.setText("NOT YET IMPLEMENTED");
		unimplementedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		firstSubPanel.add(unimplementedLabel);
		panel.add(firstSubPanel);
		JPanel secondSubPanel = new JPanel();
		secondSubPanel.setLayout(new GridLayout(3, 1));
		this.commandPanel = new CommandPanel();
		secondSubPanel.add(this.commandPanel);
		this.messagePanel = new MessagePanel();
		secondSubPanel.add(this.messagePanel);
		this.imagePanel = new ImagePanel();
		secondSubPanel.add(this.imagePanel);
		panel.add(secondSubPanel);
		this.frame.add(panel, BorderLayout.CENTER);
		this.frame.setSize(600, 400);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}
}

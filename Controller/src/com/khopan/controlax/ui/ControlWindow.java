package com.khopan.controlax.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.khopan.controlax.ui.command.CommandPanel;
import com.khopan.controlax.ui.image.ImagePanel;

public class ControlWindow {
	public final JFrame frame;
	public final CommandPanel commandPanel;
	public final ImagePanel imagePanel;

	public ControlWindow() {
		this.frame = new JFrame();
		this.frame.setTitle("Control Panel");
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setLayout(new GridLayout(2, 1));
		this.commandPanel = new CommandPanel();
		panel.add(this.commandPanel);
		this.imagePanel = new ImagePanel();
		panel.add(this.imagePanel);
		this.frame.add(panel, BorderLayout.CENTER);
		this.frame.setSize(600, 400);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}
}

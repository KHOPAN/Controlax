package com.khopan.controlax.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.khopan.controlax.ControlaxServer;

public class Window {
	public final JFrame frame;
	public final JLabel label;

	public Window() throws Throwable {
		this.frame = new JFrame();
		this.frame.setTitle("IP Address Viewer");
		this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		this.label = new JLabel();
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		this.label.setText(ControlaxServer.INSTANCE.localHost.getHostAddress());
		this.frame.add(this.label, BorderLayout.CENTER);
		this.frame.setSize(200, 100);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}
}

package com.khopan.controlax.ui.chat;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ChatWindow {
	public final JFrame frame;

	public ChatWindow() {
		this.frame = new JFrame();
		this.frame.setTitle("Chat with Target");
		this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.frame.setSize(600, 400);
		this.frame.setLocationRelativeTo(null);
	}

	public void open() {
		this.frame.setVisible(true);
	}

	public void close() {
		this.frame.dispose();
	}
}

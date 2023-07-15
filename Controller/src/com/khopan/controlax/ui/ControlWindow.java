package com.khopan.controlax.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.ui.chat.ChatWindow;
import com.khopan.controlax.ui.color.ColorPanel;
import com.khopan.controlax.ui.command.CommandPanel;
import com.khopan.controlax.ui.controlling.ControllingPanel;
import com.khopan.controlax.ui.image.ScreenshotPanel;
import com.khopan.controlax.ui.image.StreamRenderer;
import com.khopan.controlax.ui.message.MessagePanel;

public class ControlWindow {
	public final ChatWindow chatWindow;
	public final JFrame frame;
	public final JTextPane statusPane;
	public final StreamRenderer streamRenderer;
	public final ColorPanel colorPanel;
	public final CommandPanel commandPanel;
	public final MessagePanel messagePanel;
	public final ScreenshotPanel screenshotPanel;
	public final ControllingPanel controllingPanel;

	public ControlWindow() {
		this.chatWindow = new ChatWindow();
		this.statusPane = new JTextPane();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable Errors) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				Errors.printStackTrace(printWriter);
				String text = stringWriter.toString();
				ControlWindow.this.status(text);
			}
		});

		this.frame = new JFrame();
		this.frame.setTitle("Control Panel");
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int empty = (int) Math.round((((double) size.width) + ((double) size.height)) * 0.00937207123d);
		panel.setBorder(new EmptyBorder(empty, empty, empty, empty));
		panel.setLayout(new GridLayout(1, 2));
		JPanel firstSubPanel = new JPanel();
		firstSubPanel.setLayout(new GridLayout(2, 1));
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());
		statusPanel.setBorder(new TitledBorder("Output"));
		JScrollPane scrollPane = new JScrollPane(this.statusPane);
		statusPanel.add(scrollPane, BorderLayout.CENTER);
		firstSubPanel.add(statusPanel);
		this.streamRenderer = new StreamRenderer();
		firstSubPanel.add(this.streamRenderer);
		panel.add(firstSubPanel);
		JPanel secondSubPanel = new JPanel();
		secondSubPanel.setLayout(new GridLayout(3, 1));
		JPanel colorCommandPanel = new JPanel();
		colorCommandPanel.setLayout(new GridLayout(1, 2));
		this.colorPanel = new ColorPanel();
		colorCommandPanel.add(this.colorPanel);
		this.commandPanel = new CommandPanel();
		colorCommandPanel.add(this.commandPanel);
		secondSubPanel.add(colorCommandPanel);
		JPanel screenshotMessagePanel = new JPanel();
		screenshotMessagePanel.setLayout(new GridLayout(1, 2));
		this.messagePanel = new MessagePanel();
		screenshotMessagePanel.add(this.messagePanel);
		this.screenshotPanel = new ScreenshotPanel();
		screenshotMessagePanel.add(this.screenshotPanel);
		secondSubPanel.add(screenshotMessagePanel);
		this.controllingPanel = new ControllingPanel();
		secondSubPanel.add(this.controllingPanel);
		panel.add(secondSubPanel);
		this.frame.add(panel, BorderLayout.CENTER);
		this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.frame.setVisible(true);
	}

	public void status(String message) {
		this.statusPane.setText(this.statusPane.getText() + message + "\n");
	}
}

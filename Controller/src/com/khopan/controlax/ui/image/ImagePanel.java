package com.khopan.controlax.ui.image;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -2841650909096430604L;

	public final ScreenshotPanel screenshotPanel;

	public ImagePanel() {
		this.setBorder(new TitledBorder("Image"));
		this.setLayout(new GridLayout(2, 1));
		this.screenshotPanel = new ScreenshotPanel();
		this.add(this.screenshotPanel);
		this.add(this.streamPanel());
	}

	private JPanel streamPanel() {
		JPanel streamPanel = new JPanel();
		streamPanel.setBorder(new TitledBorder("Stream"));
		return streamPanel;
	}
}

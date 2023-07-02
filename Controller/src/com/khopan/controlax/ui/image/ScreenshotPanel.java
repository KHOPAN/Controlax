package com.khopan.controlax.ui.image;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.packet.HeaderedImagePacket;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class ScreenshotPanel extends JPanel {
	private static final long serialVersionUID = -7327805975937257834L;

	public static final byte SCREENSHOT_HEADER = 0x21;

	public final JButton takeScreenshotButton;
	public final JButton viewScreenshotButton;

	public BufferedImage screenshot;

	public ScreenshotPanel() {
		this.setBorder(new TitledBorder("Screenshot"));
		this.setLayout(new GridLayout(2, 1));
		this.takeScreenshotButton = new JButton();
		this.takeScreenshotButton.setText("Take Screenshot");
		this.takeScreenshotButton.addActionListener(Event -> this.takeScreenshot());
		this.add(this.takeScreenshotButton);
		this.viewScreenshotButton = new JButton();
		this.viewScreenshotButton.setText("View Screenshot");
		this.viewScreenshotButton.addActionListener(Event -> ImageViewerPane.viewImage(this.screenshot, "View Screenshot Image"));
		this.viewScreenshotButton.setEnabled(false);
		this.add(this.viewScreenshotButton);
	}

	private void takeScreenshot() {
		Controlax.INSTANCE.window.status("Taking the screenshot...");
		BinaryConfigObject config = new BinaryConfigObject();
		config.putInt("Action", 3);
		Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
	}

	public void processImagePacket(HeaderedImagePacket packet) {
		this.screenshot = packet.getImage();
		Controlax.INSTANCE.window.status("Done taking the screenshot");
		this.viewScreenshotButton.setEnabled(true);
	}
}

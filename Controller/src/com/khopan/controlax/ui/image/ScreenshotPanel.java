package com.khopan.controlax.ui.image;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.packet.HeaderedImagePacket;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class ScreenshotPanel extends JPanel {
	private static final long serialVersionUID = -7327805975937257834L;

	public static final byte SCREENSHOT_HEADER = 0x21;

	public final JLabel statusLabel;
	public final JButton takeScreenshotButton;
	public final JButton viewScreenshotButton;

	public BufferedImage screenshot;
	public boolean lastResponse;

	public ScreenshotPanel() {
		this.setBorder(new TitledBorder("Screenshot"));
		this.setLayout(new GridLayout(3, 1));
		this.statusLabel = new JLabel();
		this.statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.disconnected();
		this.add(this.statusLabel);
		this.takeScreenshotButton = new JButton();
		this.takeScreenshotButton.setText("Take Screenshot");
		this.takeScreenshotButton.addActionListener(Event -> this.takeScreenshot());
		this.add(this.takeScreenshotButton);
		this.viewScreenshotButton = new JButton();
		this.viewScreenshotButton.setText("View Screenshot");
		this.viewScreenshotButton.addActionListener(Event -> ImageViewerPane.viewImage(this.screenshot, "View Screenshot Image"));
		this.viewScreenshotButton.setEnabled(false);
		this.add(this.viewScreenshotButton);
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			try {
				if(this.lastResponse) {
					this.connected();
				} else {
					this.disconnected();
				}

				this.lastResponse = false;
				BinaryConfigObject config = new BinaryConfigObject();
				config.putInt("Action", 0);
				Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
			} catch(Throwable Errors) {

			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
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

	public void connected() {
		this.statusLabel.setText("Status: Connected");
		this.statusLabel.setForeground(new Color(0x00FF00));
	}

	public void disconnected() {
		this.statusLabel.setText("Status: Disconnected");
		this.statusLabel.setForeground(new Color(0xFF0000));
	}
}

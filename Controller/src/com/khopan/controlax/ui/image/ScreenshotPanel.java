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
import com.khopan.controlax.action.action.ScreenshotAction;
import com.khopan.controlax.action.action.StatusCheckAction;
import com.khopan.controlax.packet.HeaderedImagePacket;

public class ScreenshotPanel extends JPanel {
	private static final long serialVersionUID = -7327805975937257834L;

	public static final byte SCREENSHOT_HEADER = 0x21;

	public final FullscreenStream fullscreen;
	public final JLabel statusLabel;
	public final JButton takeScreenshotButton;
	public final JButton viewScreenshotButton;
	public final JButton fullscreenButton;

	public BufferedImage screenshot;
	public boolean lastResponse;

	public ScreenshotPanel() {
		this.fullscreen = new FullscreenStream();
		this.setBorder(new TitledBorder("Screenshot"));
		this.setLayout(new GridLayout(4, 1));
		this.statusLabel = new JLabel();
		this.statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.statusLabel.setText("Status: Undefined");
		this.statusLabel.setForeground(new Color(0x7F7F7F));
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
		this.fullscreenButton = new JButton();
		this.fullscreenButton.setText("Stream Fullscreen");
		this.fullscreenButton.addActionListener(Event -> this.fullscreen.show());
		this.add(this.fullscreenButton);
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			String text;
			int color;

			if(this.lastResponse) {
				text = "Status: Connected";
				color = 0x00FF00;
			} else {
				text = "Status: Disconnected";
				color = 0xFF0000;
			}

			this.statusLabel.setText(text);
			this.statusLabel.setForeground(new Color(color));
			this.lastResponse = false;
			Controlax.INSTANCE.processor.sendAction(StatusCheckAction.getInstance());
		}, 0, 1000, TimeUnit.MILLISECONDS);
	}

	private void takeScreenshot() {
		Controlax.INSTANCE.window.logNormal("Screenshot: Sent");
		Controlax.INSTANCE.processor.sendAction(ScreenshotAction.getInstance());
	}

	public void processImagePacket(HeaderedImagePacket packet) {
		this.screenshot = packet.getImage();
		Controlax.INSTANCE.window.logNormal("Screenshot: Received");
		this.viewScreenshotButton.setEnabled(true);
	}
}

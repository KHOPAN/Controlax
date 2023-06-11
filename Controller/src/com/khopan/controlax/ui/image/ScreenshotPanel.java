package com.khopan.controlax.ui.image;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.ControlaxServer;
import com.khopan.controlax.ImageViewerPane;
import com.khopan.lazel.Packet;
import com.khopan.lazel.config.BinaryConfigObject;

public class ScreenshotPanel extends JPanel {
	private static final long serialVersionUID = -7327805975937257834L;

	public final JTextPane screenshotStatusPane;
	public final JButton takeScreenshotButton;
	public final JButton viewScreenshotButton;

	public BufferedImage screenshot;

	public ScreenshotPanel() {
		this.setBorder(new TitledBorder("Screenshot"));
		this.setLayout(new GridLayout(2, 1));
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new TitledBorder("Status"));
		statusPanel.setLayout(new BorderLayout());
		this.screenshotStatusPane = new JTextPane();
		JScrollPane statusScrollPane = new JScrollPane(this.screenshotStatusPane);
		statusPanel.add(statusScrollPane, BorderLayout.CENTER);
		this.add(statusPanel);
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(1, 2));
		this.takeScreenshotButton = new JButton();
		this.takeScreenshotButton.setText("Take Screenshot");
		this.takeScreenshotButton.addActionListener(Event -> this.takeScreenshot());
		controlPanel.add(this.takeScreenshotButton);
		this.viewScreenshotButton = new JButton();
		this.viewScreenshotButton.setText("View Screenshot");
		this.viewScreenshotButton.addActionListener(Event -> ImageViewerPane.viewImage(this.screenshot));
		controlPanel.add(this.viewScreenshotButton);
		this.add(controlPanel);
	}

	private void takeScreenshot() {
		this.screenshotStatusPane.setText("Sending Take Screenshot Command");
		BinaryConfigObject config = new BinaryConfigObject();
		config.putInt("Action", 2);
		ControlaxServer.INSTANCE.gateway.sendPacket(new Packet(config));
	}

	public void processImagePacket(Packet packet) {
		System.out.println("Length: " + packet.getRawData().length);

		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(packet.getRawData()));
			this.screenshotStatusPane.setText("Image Captured Successfully!");
			this.screenshot = image;
		} catch(Throwable Errors) {
			Errors.printStackTrace();
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Error while reconstructing the image", Errors).printStackTrace(printWriter);
			this.screenshotStatusPane.setText(stringWriter.toString());
		}
	}
}

package com.khopan.controlax;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.khopan.controlax.packet.HeaderedImagePacket;

public class ImageProcessor {
	public static final byte SCREENSHOT_HEADER = 0x21;
	public static final byte STREAM_HEADER = 0x5F;

	private static BufferedImage CursorImage;

	public static void processScreenshot() {
		HeaderedImagePacket packet = new HeaderedImagePacket(ImageProcessor.takeScreenshot(), ImageProcessor.SCREENSHOT_HEADER, 0);
		Controlax.INSTANCE.sendPacket(packet);
	}

	public static BufferedImage takeScreenshot() {
		Rectangle area = new Rectangle();
		area.x = area.y = 0;
		area.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		double average = (int) Math.round((((double) area.width) + ((double) area.height)) * 0.5d);
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		BufferedImage image = Controlax.INSTANCE.robot.createScreenCapture(area);
		Graphics2D Graphics = image.createGraphics();
		Graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics.drawImage(ImageProcessor.CursorImage.getScaledInstance((int) Math.round(average * 0.0112464855d), (int) Math.round(average * 0.0178069353d), BufferedImage.SCALE_SMOOTH), mouse.x, mouse.y, null);
		Graphics.dispose();
		return image;
	}

	public static void load() {
		try {
			ImageProcessor.CursorImage = ImageIO.read(ImageProcessor.class.getResource("cursor.png"));
		} catch(Throwable Errors) {
			Errors.printStackTrace();
		}
	}
}

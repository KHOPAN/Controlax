package com.khopan.controlax;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.khopan.controlax.action.action.ScreenshotAction;
import com.khopan.controlax.packet.HeaderedImagePacket;

public class ImageProcessor {
	public static final byte SCREENSHOT_HEADER = 0x21;
	public static final byte STREAM_HEADER = 0x5F;

	private static BufferedImage CursorImage;

	public static void process(ScreenshotAction action) {
		ImageProcessor.sendScreenshot(ImageProcessor.SCREENSHOT_HEADER);
	}

	public static void sendScreenshot(byte header) {
		Controlax.INSTANCE.sendPacket(new HeaderedImagePacket(ImageProcessor.screenshot(), header));
	}

	public static BufferedImage screenshot() {
		Rectangle area = new Rectangle();
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

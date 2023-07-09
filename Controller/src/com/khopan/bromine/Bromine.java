package com.khopan.bromine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Bromine {
	private Bromine() {}

	public static final Dimension MONITOR_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

	public static void initialize(BromineApplication application) {
		if(application == null) {
			throw new NullPointerException("Application cannot be null");
		}

		application.window = new Window();
		application.initialize();
	}

	public static BufferedImage getLogo(int size) {
		return Bromine.getLogo(size, 0x000000);
	}

	public static BufferedImage getLogo(int size, int color) {
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D Graphics = image.createGraphics();
		Graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics.setColor(new Color(color));
		Graphics.fill(new BromineLogo(size));
		Graphics.dispose();
		return image;
	}
}

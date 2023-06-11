package com.khopan.controlax;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ImageViewerPane {
	private ImageViewerPane(BufferedImage image) {
		System.out.println(image);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(new ImagePane(image));
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void viewImage(BufferedImage image) {
		new ImageViewerPane(image);
	}

	private class ImagePane extends Component {
		private static final long serialVersionUID = 2269953635899604215L;

		private final BufferedImage image;

		private int width;
		private int height;

		private ImagePane(BufferedImage image) {
			this.image = image;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void reshape(int x, int y, int width, int height) {
			super.reshape(x, y, width, height);
			this.width = width;
			this.height = height;
		}

		@Override
		public void paint(Graphics Graphics) {
			Graphics2D Graphics2D = (Graphics2D) Graphics;
			Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Graphics2D.drawImage(this.image.getScaledInstance(this.width, this.height, BufferedImage.SCALE_SMOOTH), 0, 0, null);
		}
	}
}

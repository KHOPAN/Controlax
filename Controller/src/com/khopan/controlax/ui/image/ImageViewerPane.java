package com.khopan.controlax.ui.image;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ImageViewerPane {
	public ImagePane pane;

	private ImageViewerPane(BufferedImage image, String title) {
		JFrame frame = new JFrame();
		frame.setTitle(title);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		this.pane = new ImagePane(image);
		frame.add(this.pane);
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static ImageViewerPane viewImage(BufferedImage image, String title) {
		return new ImageViewerPane(image, title);
	}

	public class ImagePane extends Component {
		private static final long serialVersionUID = 2269953635899604215L;

		private BufferedImage image;
		private Image renderingImage;
		private int width;
		private int height;

		private ImagePane(BufferedImage image) {
			this.image = image;
		}

		public void updateImage(BufferedImage image) {
			this.image = image;

			if(this.image != null) {
				this.renderingImage = this.image.getScaledInstance(this.width, this.height, BufferedImage.SCALE_FAST);
				this.repaint();
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		public void reshape(int x, int y, int width, int height) {
			super.reshape(x, y, width, height);

			if(this.width != width || this.height != height) {
				this.width = width;
				this.height = height;

				if(this.image != null) {
					this.renderingImage = this.image.getScaledInstance(this.width, this.height, BufferedImage.SCALE_SMOOTH);
				}
			}
		}

		@Override
		public void paint(Graphics Graphics) {
			Graphics2D Graphics2D = (Graphics2D) Graphics;
			Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Graphics2D.drawImage(this.renderingImage, 0, 0, null);
		}
	}
}

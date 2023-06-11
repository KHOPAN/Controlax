package com.khopan.controlax.ui.color;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ColorDisplay {
	private static final ColorDisplay DISPLAY = new ColorDisplay();

	private final JFrame frame;
	private final Pane pane;

	private ColorDisplay() {
		this.pane = new Pane();
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.frame.setUndecorated(true);
		this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.frame.setLayout(new BorderLayout());
		this.frame.add(this.pane, BorderLayout.CENTER);
		this.frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank"));
	}

	public static void clear() {
		if(ColorDisplay.DISPLAY.frame.isVisible()) {
			ColorDisplay.DISPLAY.frame.dispose();
		}
	}

	public static void display(int color) {
		ColorDisplay.DISPLAY.pane.mode = false;
		ColorDisplay.DISPLAY.pane.color = color;
		ColorDisplay.DISPLAY.frame.setVisible(true);
	}

	public static void rainbow(boolean moving) {
		ColorDisplay.DISPLAY.pane.mode = true;
		ColorDisplay.DISPLAY.pane.moving = moving;
		ColorDisplay.DISPLAY.frame.setVisible(true);
	}

	public static void transparency(float transparency) {
		ColorDisplay.DISPLAY.frame.setOpacity(transparency);
	}

	public static void rainbowRate(float rate) {
		ColorDisplay.DISPLAY.pane.rate = rate;
	}

	private class Pane extends Component {
		private static final long serialVersionUID = -5438163172889761511L;

		private int width;
		private int height;

		private boolean mode;
		private int color;
		private boolean moving;

		private float offset;
		private float rate;

		private Pane() {
			this.rate = 0.0001f;

			Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
				this.offset += this.rate;

				if(this.moving) {
					this.repaint();
				}
			}, 0, 1, TimeUnit.MILLISECONDS);
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

			if(!this.mode) {
				Graphics2D.setColor(new Color(this.color));
				Graphics2D.fillRect(0, 0, this.width, this.height);
			} else {
				Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				int loop = Math.max(this.width, this.height) + Math.min(this.width, this.height);

				for(int i = 0; i < loop; i++) {
					float hue = ((float) (((double) i) / ((double) loop))) + (this.moving ? this.offset : 0.0f);

					while(hue < 0.0f) {
						hue += 1.0f;
					}

					while(hue > 0.0f) {
						hue -= 1.0f;
					}

					Graphics2D.setColor(Color.getHSBColor(hue, 1.0f, 1.0f));
					Graphics2D.drawLine(0, i, i, 0);
				}
			}
		}
	}
}

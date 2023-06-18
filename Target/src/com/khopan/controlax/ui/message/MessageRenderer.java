package com.khopan.controlax.ui.message;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JWindow;

import com.khopan.animation.interpolator.Interpolator;
import com.khopan.animation.transform.DoubleTransform;
import com.khopan.controlax.Controlax;

public class MessageRenderer {
	private final JWindow frame;
	private final Rectangle maxWindowSize;
	private final MessagePane pane;

	private MessageRenderer(String message) {
		Font font = new JLabel().getFont();
		FontMetrics metrics = new JLabel().getFontMetrics(font);
		int fontHeight = metrics.getHeight();
		String[] messageList = message.split("\n");
		int width = 0;

		for(int i = 0; i < messageList.length; i++) {
			width = Math.max(width, metrics.stringWidth(messageList[i]));
		}

		width += fontHeight * 2;
		int height = (int) Math.round(((double) messageList.length) * ((double) fontHeight) + (((double) messageList.length) - 1.0d) * (((double) fontHeight) * 0.5d) + ((double) fontHeight) * 1.5d);
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int spacing = (int) Math.round((((double) size.width) + ((double) size.height)) * 0.00702905342d);
		this.frame = new JWindow();
		this.pane = new MessagePane(width, height, messageList, font, spacing);
		this.frame.setContentPane(this.pane);
		this.frame.setBackground(new Color(0, 0, 0, 0));
		this.maxWindowSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.frame.setBounds(this.maxWindowSize.width - width - spacing, this.maxWindowSize.height - height - spacing, width + spacing, height + spacing);
		this.frame.setAlwaysOnTop(true);
		this.frame.setVisible(true);
	}

	private static Shape createShadow(Shape shape, double shadowDistance) {
		Area shapeArea = new Area(shape);
		Area shadowArea = new Area(shape);
		AffineTransform transform = new AffineTransform();
		transform.translate(shadowDistance, shadowDistance);
		shadowArea.transform(transform);
		shadowArea.subtract(shapeArea);
		return shadowArea;
	}

	public static void showMessage(String message) {
		new MessageRenderer(message);
	}

	private class MessagePane extends JComponent {
		private static final long serialVersionUID = 1590373204526394047L;

		private final String[] message;
		private final DoubleTransform transform;

		private int width;
		private int height;
		private Font font;
		private double translate;
		private boolean end;

		private MessagePane(int width, int height, String[] message, Font font, int spacing) {
			this.setOpaque(false);
			this.message = message;
			this.transform = new DoubleTransform();
			this.transform.framerate().set(Controlax.getFramerate());
			this.transform.duration().set(1500);
			this.transform.interpolator().set(Interpolator.CUBIC_EASE_IN_OUT);
			int maxTranslate = width + spacing;
			this.transform.valueUpdater().set(value -> {
				this.translate = ((double) maxTranslate) * (this.end ? value : (1.0d - value));
				this.repaint();

				if(value == 0.0d && this.end) {
					MessageRenderer.this.frame.dispose();
				}

				if(value == 1.0d && !this.end) {
					try {
						Thread.sleep(5000);
					} catch(Throwable Errors) {

					}

					this.transform.begin(1.0d, 0.0d);
					this.end = true;
				}
			});

			this.transform.begin(0.0d, 1.0d);
			this.width = width;
			this.height = height;
			this.font = font;
		}

		@Override
		public void paint(Graphics Graphics) {
			Graphics2D Graphics2D = (Graphics2D) Graphics;
			Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Graphics2D.translate(this.translate, 0.0d);
			Graphics2D.setColor(new Color(0xAAAAAA));
			int arc = (int) Math.round(Math.min(((double) this.width), ((double) this.height)) * 0.428571429d);
			RoundRectangle2D.Double rectangle = new RoundRectangle2D.Double(0, 0, this.width, this.height, arc, arc);
			Graphics2D.fill(rectangle);
			Graphics2D.setColor(new Color(0xFFFFFF));
			int border = 1;
			Graphics2D.fill(new RoundRectangle2D.Double(rectangle.x + border, rectangle.y + border, rectangle.width - border * 2, rectangle.height - border * 2, rectangle.arcwidth - border * 2, rectangle.archeight - border * 2));
			Graphics2D.setColor(new Color(0, 0, 0, 128));
			Graphics2D.fill(MessageRenderer.createShadow(rectangle, 2.0d));
			Graphics2D.setColor(new Color(0x000000));
			Graphics2D.setFont(this.font);
			FontMetrics metrics = Graphics2D.getFontMetrics();
			int size = this.message.length;
			int longest = 0;

			for(int i = 0; i < size; i++) {
				longest = Math.max(longest, metrics.stringWidth(this.message[i]));
			}

			double cell = ((double) this.height) / (((double) size) + 1.0d);
			int textX = (int) Math.round((((double) width) - ((double) longest)) * 0.5d);
			double textY = 0;

			for(int i = 0; i < this.message.length; i++) {
				textY += cell;
				String line = this.message[i];
				Graphics2D.drawString(line, textX, (int) Math.round(textY - (((double) metrics.getHeight()) * 0.5d) + metrics.getAscent()));
			}
		}
	}
}

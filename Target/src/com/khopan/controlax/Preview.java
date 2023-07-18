package com.khopan.controlax;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Preview {
	public static void preview(BufferedImage image) {
		if(image == null) {
			throw new NullPointerException("Image cannot be null");
		}

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(new PreviewPane(image));
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static class PreviewPane extends Component {
		private static final long serialVersionUID = -9086671486218362603L;

		private final BufferedImage image;
		private final int width;
		private final int height;
		private final Ratio ratio;
		private final Image scaledImage;
		private final Dimension size;

		private PreviewPane(BufferedImage image) {
			this.image = image;
			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
			this.ratio = this.reduceRatio(this.width, this.height);
			this.size = this.solveForSize(this.ratio);
			this.scaledImage = this.image.getScaledInstance(this.size.width, this.size.height, BufferedImage.SCALE_SMOOTH);
			this.setPreferredSize(this.size);
		}

		@Override
		public void paint(Graphics Graphics) {
			Graphics2D Graphics2D = (Graphics2D) Graphics;
			Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Graphics2D.drawImage(this.scaledImage, 0, 0, null);
		}

		private Dimension solveForSize(Ratio ratio) {
			Dimension size = new Dimension();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int defaultSize = (int) Math.round(Math.min(((double) screenSize.width), ((double) screenSize.height)) * 0.75d);

			if(ratio.numerator > ratio.denominator) {
				size.width = defaultSize;
				size.height = (int) Math.round(((double) size.width) / (((double) ratio.numerator) / ((double) ratio.denominator)));
			} else {
				size.height = defaultSize;
				size.width = (int) Math.round(((double) size.height) * (((double) ratio.numerator) / ((double) ratio.denominator)));
			}

			return size;
		}

		private Ratio reduceRatio(double numerator, double denominator) {
			double temp;
			double divisor;
			int resultNumerator;
			int resultDenominator;
			boolean defined = false;

			if(numerator == denominator) {
				Ratio ratio = new Ratio();
				ratio.numerator = ratio.denominator = 1;
				return ratio;
			}

			if(numerator < denominator) {
				temp = numerator;
				numerator = denominator;
				denominator = temp;
				defined = true;
			}

			divisor = this.greatestCommonDivider(numerator, denominator);

			if(!defined) {
				resultNumerator = (int) Math.round(numerator / divisor);
				resultDenominator = (int) Math.round(denominator / divisor);
			} else {
				resultNumerator = (int) Math.round(denominator / divisor);
				resultDenominator = (int) Math.round(numerator / divisor);
			}

			if(8 == resultNumerator && 5 == resultDenominator) {
				resultNumerator = 16;
				resultDenominator = 10;
			}

			Ratio ratio = new Ratio();
			ratio.numerator = resultNumerator;
			ratio.denominator = resultDenominator;
			return ratio;
		}

		private double greatestCommonDivider(double a, double b) {
			if(b == 0.0d) {
				return a;
			}

			return this.greatestCommonDivider(b, a % b);
		}

		private static class Ratio {
			private int numerator;
			private int denominator;
		}
	}
}

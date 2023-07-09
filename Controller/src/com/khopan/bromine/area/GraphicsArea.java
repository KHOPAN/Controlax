package com.khopan.bromine.area;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;

public class GraphicsArea implements Area {
	private final Graphics Graphics;
	private final int width;
	private final int height;
	private int thickness;
	private Mode mode;
	private ColorFilter filter;

	public GraphicsArea(Graphics Graphics, int width, int height) {
		this.Graphics = Graphics;
		this.width = width;
		this.height = height;
	}

	public GraphicsArea(Graphics Graphics, Dimension size) {
		this(Graphics, size.width, size.height);
	}

	@Override
	public void normal() {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public void smooth() {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public void hint(Key key, Object value) {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.setRenderingHint(key, value);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public void color(int color) {
		this.Graphics.setColor(this.filter == null ? new Color(color) : new Color(
				this.limit((int) Math.round(((double) ((color >> 16) & 0xFF)) * this.filter.filterRed)),
				this.limit((int) Math.round(((double) ((color >> 8) & 0xFF)) * this.filter.filterGreen)),
				this.limit((int) Math.round(((double) (color & 0xFF)) * this.filter.filterBlue))
				));
	}

	@Override
	public void color(int red, int green, int blue) {
		this.Graphics.setColor(this.filter == null ? new Color(red, green, blue) : new Color(
				this.limit((int) Math.round(((double) red) * this.filter.filterRed)),
				this.limit((int) Math.round(((double) green) * this.filter.filterGreen)),
				this.limit((int) Math.round(((double) blue) * this.filter.filterBlue))
				));
	}

	@Override
	public void color(int red, int green, int blue, int alpha) {
		this.Graphics.setColor(this.filter == null ? new Color(red, green, blue, alpha) : new Color(
				this.limit((int) Math.round(((double) red) * this.filter.filterRed)),
				this.limit((int) Math.round(((double) green) * this.filter.filterGreen)),
				this.limit((int) Math.round(((double) blue) * this.filter.filterBlue)),
				this.limit((int) Math.round(((double) alpha) * this.filter.filterAlpha))
				));
	}

	@Override
	public void color(Color color) {
		this.Graphics.setColor(this.filter == null ? color : new Color(
				this.limit((int) Math.round(((double) color.getRed()) * this.filter.filterRed)),
				this.limit((int) Math.round(((double) color.getGreen()) * this.filter.filterGreen)),
				this.limit((int) Math.round(((double) color.getBlue()) * this.filter.filterBlue)),
				this.limit((int) Math.round(((double) color.getAlpha()) * this.filter.filterAlpha))
				));
	}

	@Override
	public void mode(Mode mode) {
		this.mode = mode;
	}

	@Override
	public void draw() {
		this.mode = Mode.DRAW;
	}

	@Override
	public void fill() {
		this.mode = Mode.FILL;
	}

	@Override
	public void thickness(int thickness) {
		this.thickness = thickness;
	}

	@Override
	public void font(Font font) {
		this.Graphics.setFont(font);
	}

	@Override
	public void composite(Composite composite) {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.setComposite(composite);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public void stroke(Stroke stroke) {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.setStroke(stroke);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public void paint(Paint paint) {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.setPaint(paint);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public Color color() {
		return this.Graphics.getColor();
	}

	@Override
	public int intColor() {
		return this.Graphics.getColor().getRGB();
	}

	@Override
	public int[] separateColor() {
		Color color = this.Graphics.getColor();
		return new int[] {color.getRed(), color.getGreen(), color.getBlue()};
	}

	@Override
	public int[] alphaColor() {
		Color color = this.Graphics.getColor();
		return new int[] {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
	}

	@Override
	public Mode mode() {
		return this.mode;
	}

	@Override
	public int thickness() {
		return this.thickness;
	}

	@Override
	public Font font() {
		return this.Graphics.getFont();
	}

	@Override
	public Composite composite() {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			return Graphics2D.getComposite();
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public Stroke stroke() {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			return Graphics2D.getStroke();
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public Paint paint() {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			return Graphics2D.getPaint();
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public Area create() {
		GraphicsArea area = new GraphicsArea(this.Graphics.create(), this.width, this.height);
		area.filter = this.filter;
		return area;
	}

	@Override
	public Area create(int x, int y, int width, int height) {
		GraphicsArea area = new GraphicsArea(this.Graphics.create(x, y, width, height), width, height);
		area.filter = this.filter;
		return area;
	}

	@Override
	public Area create(Rectangle bounds) {
		return this.create(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public Area create(int x, int y, Dimension size) {
		return this.create(x, y, size.width, size.height);
	}

	@Override
	public Area create(Point location, int width, int height) {
		return this.create(location.x, location.y, width, height);
	}

	@Override
	public Area create(Point location, Dimension size) {
		return this.create(location.x, location.y, size.width, size.height);
	}

	@Override
	public void done() {
		this.Graphics.dispose();
	}

	@Override
	public void rect(int x, int y, int width, int height) {
		if(Mode.FILL.equals(this.mode)) {
			this.Graphics.fillRect(x, y, width, height);
		} else {
			this.Graphics.drawRect(x, y, width, height);
		}
	}

	@Override
	public void rect(Rectangle rectangle) {
		this.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	@Override
	public void rect(int x, int y, Dimension size) {
		this.rect(x, y, size.width, size.height);
	}

	@Override
	public void rect(Point location, int width, int height) {
		this.rect(location.x, location.y, width, height);
	}

	@Override
	public void rect(Point location, Dimension size) {
		this.rect(location.x, location.y, size.width, size.height);
	}

	@Override
	public void square(int x, int y, int size) {
		this.rect(x, y, size, size);
	}

	@Override
	public void square(Point location, int size) {
		this.rect(location.x, location.y, size, size);
	}

	@Override
	public void oval(int x, int y, int width, int height) {
		if(Mode.FILL.equals(this.mode)) {
			this.Graphics.fillOval(x, y, width, height);
		} else {
			this.Graphics.drawOval(x, y, width, height);
		}
	}

	@Override
	public void oval(Rectangle bounds) {
		this.oval(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public void oval(int x, int y, Dimension size) {
		this.oval(x, y, size.width, size.height);
	}

	@Override
	public void oval(Point location, int width, int height) {
		this.oval(location.x, location.y, width, height);
	}

	@Override
	public void oval(Point location, Dimension size) {
		this.oval(location.x, location.y, size.width, size.height);
	}

	@Override
	public void circle(int x, int y, int radius) {
		int size = radius * 2;
		this.oval(x - radius, y - radius, size, size);
	}

	@Override
	public void circle(Point location, int radius) {
		int size = radius * 2;
		this.oval(location.x - radius, location.y - radius, size, size);
	}

	@Override
	public void circleDiameter(int x, int y, int diameter) {
		this.oval(x, y, diameter, diameter);
	}

	@Override
	public void circleDiameter(Point location, int diameter) {
		this.oval(location.x, location.y, diameter, diameter);
	}

	@Override
	public void polygon(int[] xPoints, int[] yPoints, int nPoints) {
		if(Mode.FILL.equals(this.mode)) {
			this.Graphics.fillPolygon(xPoints, yPoints, nPoints);
		} else {
			this.Graphics.drawPolygon(xPoints, yPoints, nPoints);
		}
	}

	@Override
	public void text(String text, int x, int y) {
		this.Graphics.drawString(text, x, y);
	}

	@Override
	public void textCenter(String text) {
		FontMetrics metrics = this.Graphics.getFontMetrics();
		this.Graphics.drawString(text, (int) Math.round((((double) this.width) - ((double) metrics.stringWidth(text))) * 0.5d), (int) Math.round((((double) this.height) - ((double) metrics.getHeight())) * 0.5d + ((double) metrics.getAscent())));
	}

	@Override
	public void textCenterX(String text, int y) {
		FontMetrics metrics = this.Graphics.getFontMetrics();
		this.Graphics.drawString(text, (int) Math.round((((double) this.width) - ((double) metrics.stringWidth(text))) * 0.5d), y);
	}

	@Override
	public void textCenterY(String text, int x) {
		FontMetrics metrics = this.Graphics.getFontMetrics();
		this.Graphics.drawString(text, x, (int) Math.round((((double) this.height) - ((double) metrics.getHeight())) * 0.5d + ((double) metrics.getAscent())));
	}

	@Override
	public Point textCenterLocation(String text) {
		FontMetrics metrics = this.Graphics.getFontMetrics();
		return new Point((int) Math.round((((double) this.width) - ((double) metrics.stringWidth(text))) * 0.5d), (int) Math.round((((double) this.height) - ((double) metrics.getHeight())) * 0.5d + ((double) metrics.getAscent())));
	}

	@Override
	public int textCenterX(String text) {
		return (int) Math.round((((double) this.width) - ((double) this.Graphics.getFontMetrics().stringWidth(text))) * 0.5d);
	}

	@Override
	public int textCenterY() {
		FontMetrics metrics = this.Graphics.getFontMetrics();
		return (int) Math.round((((double) this.height) - ((double) metrics.getHeight())) * 0.5d + ((double) metrics.getAscent()));
	}

	@Override
	public void image(Image image) {
		this.Graphics.drawImage(image, 0, 0, null);
	}

	@Override
	public void image(Image image, int x, int y) {
		this.Graphics.drawImage(image, x, y, null);
	}

	@Override
	public void imageCenter(Image image) {
		this.Graphics.drawImage(image, (int) Math.round((((double) this.width) - ((double) image.getWidth(null))) * 0.5d), (int) Math.round((((double) this.height) - ((double) image.getHeight(null))) * 0.5d), null);
	}

	@Override
	public void image(Image image, int x, int y, int width, int height) {
		this.Graphics.drawImage(image, x, y, width, height, null);
	}

	@Override
	public void imageSmooth(Image image, int x, int y, int width, int height) {
		if(width != image.getWidth(null) || height != image.getHeight(null)) {
			image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}

		this.Graphics.drawImage(image, x, y, null);
	}

	@Override
	public void imageCenter(Image image, int width, int height) {
		this.Graphics.drawImage(image, (int) Math.round((((double) this.width) - ((double) width)) * 0.5d), (int) Math.round((((double) this.height) - ((double) height)) * 0.5d), width, height, null);
	}

	@Override
	public void imageSmoothCenter(Image image, int width, int height) {
		if(width != image.getWidth(null) || height != image.getHeight(null)) {
			image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}

		this.Graphics.drawImage(image, (int) Math.round((((double) this.width) - ((double) width)) * 0.5d), (int) Math.round((((double) this.height) - ((double) height)) * 0.5d), null);
	}

	@Override
	public void imageFull(Image image) {
		this.Graphics.drawImage(image, 0, 0, this.width, this.height, null);
	}

	@Override
	public void imageFullSmooth(Image image) {
		if(this.width != image.getWidth(null) || this.height != image.getHeight(null)) {
			image = image.getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
		}

		this.Graphics.drawImage(image, 0, 0, null);
	}

	@Override
	public void clip(int x, int y, int width, int height) {
		this.Graphics.clipRect(x, y, width, height);
	}

	@Override
	public void clip(Rectangle rectangle) {
		this.Graphics.clipRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	@Override
	public void clip(int x, int y, Dimension size) {
		this.Graphics.clipRect(x, y, size.width, size.height);
	}

	@Override
	public void clip(Point location, int width, int height) {
		this.Graphics.clipRect(location.x, location.y, width, height);
	}

	@Override
	public void clip(Point location, Dimension size) {
		this.Graphics.clipRect(location.x, location.y, size.width, size.height);
	}

	@Override
	public void clip(Shape shape) {
		this.Graphics.setClip(shape);
	}

	@Override
	public void rotate(double theta) {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.rotate(theta);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public void rotate(double theta, double x, double y) {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.rotate(theta, x, y);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public void fill(Shape shape) {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.fill(shape);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public void draw(Shape shape) {
		if(this.Graphics instanceof Graphics2D Graphics2D) {
			Graphics2D.draw(shape);
		} else {
			throw new InternalError("Method does not supported in the current graphics.");
		}
	}

	@Override
	public void translate(int x, int y) {
		this.Graphics.translate(x, y);
	}

	@Override
	public void line(int x1, int y1, int x2, int y2) {
		this.Graphics.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void pixel(int x, int y) {
		this.Graphics.drawLine(x, y, x, y);
	}

	@Override
	public FontMetrics metrics() {
		return this.Graphics.getFontMetrics();
	}

	@Override
	public void background() {
		this.Graphics.fillRect(0, 0, this.width, this.height);
	}

	@Override
	public void background(int color) {
		this.color(color);
		this.Graphics.fillRect(0, 0, this.width, this.height);
	}

	@Override
	public void background(int red, int green, int blue) {
		this.color(red, green, blue);
		this.Graphics.fillRect(0, 0, this.width, this.height);
	}

	@Override
	public void background(int red, int green, int blue, int alpha) {
		this.color(red, green, blue, alpha);
		this.Graphics.fillRect(0, 0, this.width, this.height);
	}

	@Override
	public void background(Color color) {
		this.color(color);
		this.Graphics.fillRect(0, 0, this.width, this.height);
	}

	private int limit(int number) {
		return number < 0 ? 0 : number > 255 ? 255 : number;
	}

	@Override
	public void filter(ColorFilter filter) {
		this.filter = filter;
	}

	@Override
	public void removeFilter() {
		this.filter = null;
	}

	@Override
	public int width() {
		return this.width;
	}

	@Override
	public int height() {
		return this.height;
	}
}

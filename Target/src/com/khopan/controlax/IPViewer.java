package com.khopan.controlax;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;

public class IPViewer {
	public static final IPViewer INSTANCE = new IPViewer();

	public final Window frame;

	private final String text;

	public IPViewer() {
		this.text = Controlax.IP_ADDRESS.getHostAddress();
		this.frame = new Window(null);
		this.frame.setAlwaysOnTop(true);
		this.frame.setLayout(new BorderLayout());
		this.frame.add(new IPPane(), BorderLayout.CENTER);
		this.frame.setSize(300, 169);
		this.frame.setLocation(0, 0);
		this.frame.setVisible(true);
	}

	private class IPPane extends Component {
		private static final long serialVersionUID = -2576273116458504583L;

		private int width;
		private int height;
		private int fontSize;
		private int textX;
		private int textY;
		private Font font;

		private IPPane() {

		}

		@SuppressWarnings("deprecation")
		@Override
		public void reshape(int x, int y, int width, int height) {
			super.reshape(x, y, width, height);
			this.width = width;

			if(this.height != height && this.font != null) {
				FontMetrics metrics = this.getFontMetrics(this.font);
				this.textY = (int) Math.round((((double) this.height) - ((double) metrics.getHeight())) * 0.5d + ((double) metrics.getHeight()) - ((double) metrics.getDescent()));
			}

			this.height = height;
			int fontSize = (int) Math.round(0.102739726d * ((double) this.width));

			if(this.fontSize != fontSize) {
				this.fontSize = fontSize;
				this.font = new Font("Consolas", Font.BOLD, this.fontSize);
				FontMetrics metrics = this.getFontMetrics(this.font);
				this.textX = (int) Math.round((((double) this.width) - ((double) metrics.stringWidth(IPViewer.this.text))) * 0.5d);
				this.textY = (int) Math.round((((double) this.height) - ((double) metrics.getHeight())) * 0.5d + ((double) metrics.getHeight()) - ((double) metrics.getDescent()));
			}
		}

		@Override
		public void paint(Graphics Graphics) {
			Graphics2D Graphics2D = (Graphics2D) Graphics;
			Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Graphics2D.setColor(new Color(0xFFFFFF));
			Graphics2D.fillRect(0, 0, this.width, this.height);
			Graphics2D.setFont(this.font);
			Graphics2D.setColor(new Color(0x000000));
			Graphics2D.drawString(IPViewer.this.text, this.textX, this.textY);
		}
	}

	public static void view() {
		// Load the class only
	}
}

package com.khopan.controlax.ui.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.packet.HeaderedImagePacket;

public class StreamRenderer {
	public static final StreamRenderer INSTANCE = new StreamRenderer();

	private final JFrame frame;
	private final List<HeaderedImagePacket> imageBuffer;

	private volatile boolean start;

	private StreamRenderer() {
		this.imageBuffer = new ArrayList<>();
		this.frame = new JFrame();
		this.frame.setLayout(new BorderLayout());
		this.frame.add(new Pane(), BorderLayout.CENTER);
		this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.frame.setSize(600, 400);
		this.frame.setLocationRelativeTo(null);
	}

	public void start() {
		if(!this.start) {
			this.frame.setVisible(true);
			this.start = true;
		}
	}

	public void stop() {
		if(this.start) {
			this.start = false;
			this.frame.dispose();
		}
	}

	public void processPacket(HeaderedImagePacket packet) {
		this.imageBuffer.add(packet);
	}

	private class Pane extends Component {
		private static final long serialVersionUID = 2347735338244814851L;

		private final int framerate;
		private final long waitTime;
		private final Thread processingThread;

		private int width;
		private int height;
		private int expectedFrame;
		private BufferedImage image;
		private Image renderingImage;

		private Pane() {
			this.framerate = Controlax.getFramerate();
			this.waitTime = Math.round(1000.0d / ((double) this.framerate));
			this.expectedFrame = 1;
			this.processingThread = new Thread(this :: process);
			this.processingThread.setPriority(6);
			this.processingThread.setName("Controlax Server Stream Processing Thread");
			this.processingThread.start();
		}

		private void process() {
			while(true) {
				if(StreamRenderer.this.start) {
					HeaderedImagePacket packet;

					while((packet = this.getFrame(this.expectedFrame)) == null) {
						// Empty loop, used for waiting only
					}

					this.image = packet.getImage();
					this.renderingImage = this.image.getScaledInstance(this.width, this.height, BufferedImage.SCALE_SMOOTH);
					this.expectedFrame++;
					this.repaint();

					try {
						Thread.sleep(this.waitTime);
					} catch(Throwable Errors) {

					}
				}
			}
		}

		private HeaderedImagePacket getFrame(int frame) {
			try {
				Thread.sleep(1);
			} catch(Throwable Errors) {

			}

			for(int i = 0; i < StreamRenderer.this.imageBuffer.size(); i++) {
				if(StreamRenderer.this.imageBuffer.get(i).getFrame() == frame) {
					return StreamRenderer.this.imageBuffer.remove(i);
				}
			}

			return null;
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
			Graphics2D.setColor(new Color(0x000000));
			Graphics2D.fillRect(0, 0, this.width, this.height);
			Graphics2D.drawImage(this.renderingImage, 0, 0, null);
			Graphics2D.dispose();
		}
	}

	public static void load() {
		// Load the class only
	}
}

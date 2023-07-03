package com.khopan.controlax.ui.image;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.packet.HeaderedImagePacket;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class StreamRenderer extends Component {
	private static final long serialVersionUID = 841821419923595603L;

	public static final byte STREAM_HEADER = 0x5F;

	private final int framerate;
	private final long waitTime;
	private final List<HeaderedImagePacket> imageBuffer;
	private final Thread processingThread;
	private final Listener listener;

	private int width;
	private int height;
	private int expectedFrame;
	private BufferedImage image;
	private Image renderingImage;

	public StreamRenderer() {
		this.framerate = Controlax.getFramerate();
		this.waitTime = Math.round(1000.0d / ((double) this.framerate));
		this.expectedFrame = 1;
		this.imageBuffer = new ArrayList<>();
		this.processingThread = new Thread(this :: process);
		this.processingThread.setPriority(6);
		this.processingThread.setName("Controlax Server Stream Processing Thread");
		this.processingThread.start();
		this.listener = new Listener();
		this.addMouseListener(this.listener);
		this.addMouseMotionListener(this.listener);
	}

	private void process() {
		while(true) {
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

	public void processPacket(HeaderedImagePacket packet) {
		this.imageBuffer.add(packet);
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

	private class Listener extends MouseAdapter {
		@Override
		public void mouseMoved(MouseEvent Event) {
			if(Controlax.INSTANCE.window.controllingPanel.mouseControlBox.isSelected()) {
				BinaryConfigObject config = new BinaryConfigObject();
				config.putInt("Action", 7);
				config.putInt("SubAction", 3);
				config.putDouble("MouseX", ((double) Event.getX()) / ((double) StreamRenderer.this.width));
				config.putDouble("MouseY", ((double) Event.getY()) / ((double) StreamRenderer.this.height));
				Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
			}
		}

		@Override
		public void mouseDragged(MouseEvent Event) {
			this.mouseMoved(Event);
		}

		@Override
		public void mousePressed(MouseEvent Event) {
			if(Controlax.INSTANCE.window.controllingPanel.mouseControlBox.isSelected()) {
				BinaryConfigObject config = new BinaryConfigObject();
				config.putInt("Action", 7);
				config.putInt("SubAction", 4);
				config.putInt("Button", Event.getButton());
				Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
			}
		}

		@Override
		public void mouseReleased(MouseEvent Event) {
			if(Controlax.INSTANCE.window.controllingPanel.mouseControlBox.isSelected()) {
				BinaryConfigObject config = new BinaryConfigObject();
				config.putInt("Action", 7);
				config.putInt("SubAction", 5);
				config.putInt("Button", Event.getButton());
				Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent Event) {
			if(Controlax.INSTANCE.window.controllingPanel.mouseControlBox.isSelected()) {
				BinaryConfigObject config = new BinaryConfigObject();
				config.putInt("Action", 7);
				config.putInt("SubAction", 6);
				config.putInt("Amount", Event.getScrollAmount());
				Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
			}
		}
	}
}

package com.khopan.controlax.ui.image;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.action.action.KeyboardAction;
import com.khopan.controlax.action.action.MouseAction;
import com.khopan.controlax.packet.HeaderedImagePacket;

public class StreamRenderer extends Component {
	private static final long serialVersionUID = 841821419923595603L;

	public static final byte STREAM_HEADER = 0x5F;

	private final Listener listener;

	private int width;
	private int height;
	private BufferedImage image;
	private Image renderingImage;

	public StreamRenderer() {
		this.listener = new Listener();
		this.addMouseListener(this.listener);
		this.addMouseMotionListener(this.listener);
		this.addMouseWheelListener(this.listener);
		this.addKeyListener(this.listener);
		this.setFocusable(true);
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
		new Thread(() -> {
			BufferedImage image = packet.getImage();
			this.image = image;
			this.renderingImage = image.getScaledInstance(this.width, this.height, BufferedImage.SCALE_SMOOTH);
			this.repaint();
		}).start();
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

	private class Listener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
		private boolean pressedF9;
		private boolean pressedF10;
		private boolean pressedF11;

		@Override
		public void mouseMoved(MouseEvent Event) {
			if(Controlax.INSTANCE.window.controllingPanel.mouseControlBox.isSelected()) {
				Controlax.INSTANCE.processor.sendAction(MouseAction.getMouseMoved(this.getX(Event.getX()), this.getY(Event.getY())));
			}
		}

		@Override
		public void mouseDragged(MouseEvent Event) {
			this.mouseMoved(Event);
		}

		@Override
		public void mousePressed(MouseEvent Event) {
			if(Controlax.INSTANCE.window.controllingPanel.mouseControlBox.isSelected()) {
				Controlax.INSTANCE.processor.sendAction(MouseAction.getMousePressed(this.getX(Event.getX()), this.getY(Event.getY()), Event.getButton()));
			}
		}

		@Override
		public void mouseReleased(MouseEvent Event) {
			if(Controlax.INSTANCE.window.controllingPanel.mouseControlBox.isSelected()) {
				Controlax.INSTANCE.processor.sendAction(MouseAction.getMouseReleased(this.getX(Event.getX()), this.getY(Event.getY()), Event.getButton()));
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent Event) {
			if(Controlax.INSTANCE.window.controllingPanel.mouseControlBox.isSelected()) {
				Controlax.INSTANCE.processor.sendAction(MouseAction.getMouseWheelMoved(Event.getPreciseWheelRotation()));
			}
		}

		@Override
		public void keyTyped(KeyEvent Event) {

		}

		@Override
		public void keyPressed(KeyEvent Event) {
			int code = Event.getKeyCode();

			if(code == KeyEvent.VK_F9) {
				this.pressedF9 = true;
			} else if(code == KeyEvent.VK_F10) {
				this.pressedF10 = true;
			} else if(code == KeyEvent.VK_F11) {
				this.pressedF11 = true;
			} else {
				if(Controlax.INSTANCE.window.controllingPanel.keyboardControlBox.isSelected()) {
					Controlax.INSTANCE.processor.sendAction(KeyboardAction.getKeyPressed(code));
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent Event) {
			int code = Event.getKeyCode();

			if(code == KeyEvent.VK_F9) {
				if(this.pressedF9) {
					this.pressedF9 = true;
					Controlax.INSTANCE.window.controllingPanel.mouseControlBox.doClick();
				}
			} else if(code == KeyEvent.VK_F10) {
				if(this.pressedF10) {
					this.pressedF10 = true;
					Controlax.INSTANCE.window.controllingPanel.keyboardControlBox.doClick();
				}
			} else if(code == KeyEvent.VK_F11) {
				if(this.pressedF11) {
					this.pressedF11 = true;
					Controlax.INSTANCE.window.screenshotPanel.fullscreen.dispose();
				}
			} else {
				if(Controlax.INSTANCE.window.controllingPanel.keyboardControlBox.isSelected()) {
					Controlax.INSTANCE.processor.sendAction(KeyboardAction.getKeyReleased(code));
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent Event) {

		}

		@Override
		public void mouseEntered(MouseEvent Event) {
			this.mouseMoved(Event);
		}

		@Override
		public void mouseExited(MouseEvent Event) {
			this.mouseMoved(Event);
		}

		private double getX(int x) {
			return ((double) x) / ((double) StreamRenderer.this.width);
		}

		private double getY(int y) {
			return ((double) y) / ((double) StreamRenderer.this.height);
		}
	}
}

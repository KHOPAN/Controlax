package com.khopan.bromine;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.khopan.bromine.area.Area;
import com.khopan.bromine.area.GraphicsArea;
import com.khopan.bromine.input.Keyboard;
import com.khopan.bromine.input.Mouse;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;

public class Window extends PaneItem<Window> {
	public static final Image DEFAULT_ICON = Bromine.getLogo(32, 0xFFFFFF);

	private final JFrame frame;
	private final InnerPane pane;

	private int closeOperation;
	private String title;
	private MouseCursor cursor;
	private Image icon;

	public Window() {
		this.pane = new InnerPane();
		this.closeOperation = WindowConstants.EXIT_ON_CLOSE;
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(this.closeOperation);
		this.frame.setLayout(new BorderLayout());
		this.frame.add(this.pane, BorderLayout.CENTER);
		this.visibility = false;
		this.title().set("Bromine Application");
		this.icon().set(Window.DEFAULT_ICON);
		this.cursor = MouseCursor.DEFAULT_CURSOR;
	}

	public Property<Integer, Window> closeOperation() {
		return new SimpleProperty<Integer, Window>(() -> this.closeOperation, closeOperation -> this.closeOperation = closeOperation, closeOperation -> this.frame.setDefaultCloseOperation(closeOperation), this).nullable().whenNull(0);
	}

	public Property<String, Window> title() {
		return new SimpleProperty<String, Window>(() -> this.title, title -> this.title = title, title -> this.frame.setTitle(this.title), this).nullable().whenNull("");
	}

	@Override
	public Property<MouseCursor, Window> cursor() {
		return new SimpleProperty<MouseCursor, Window>(() -> this.cursor, cursor -> {
			this.cursor = cursor;
			this.frame.setCursor(this.cursor.cursor);
		}, this).nullable().whenNull(MouseCursor.DEFAULT_CURSOR);
	}

	public Property<Image, Window> icon() {
		return new SimpleProperty<Image, Window>(() -> this.icon, icon -> {
			this.icon = icon;
			this.frame.setIconImage(this.icon);
		}, this).nullable().whenNull(Window.DEFAULT_ICON);
	}

	@Override
	protected void onVisibilityUpdate(boolean visible) {
		this.frame.setVisible(visible);
	}

	@Override
	protected void onBoundsUpdate(Rectangle bounds) {
		super.onBoundsUpdate(bounds);
		this.frame.setBounds(this.bounds);
	}

	@Override
	public void update() {
		this.pane.repaint();
	}

	@Override
	protected void mouseAction(Mouse mouse) {
		if(this.rootMouseAction(mouse)) {
			return;
		}

		super.mouseAction(mouse);
	}

	@Override
	void unfocusAll(Item<?> exclude) {
		this.unfocus(exclude);
	}

	private class InnerPane extends Component {
		private static final long serialVersionUID = -1335428710707525420L;

		private final Listener listener;

		private int width;
		private int height;

		public InnerPane() {
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
			this.width = width;
			this.height = height;
			Window.this.bounds.x = 0;
			Window.this.bounds.y = 0;
			Window.this.bounds.width = this.width;
			Window.this.bounds.height = this.height;
			Window.this.onResize();
		}

		@Override
		public void paint(Graphics Graphics) {
			Area area = new GraphicsArea(Graphics, this.width, this.height);
			Window.this.render(area);
			Window.this.renderTop(area);
		}

		private class Listener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
			@Override
			public void keyTyped(KeyEvent Event) {
				Window.this.keyboardAction(new Keyboard(Event));
			}

			@Override
			public void keyPressed(KeyEvent Event) {
				Window.this.keyboardAction(new Keyboard(Event));
			}

			@Override
			public void keyReleased(KeyEvent Event) {
				Window.this.keyboardAction(new Keyboard(Event));
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent Event) {
				Window.this.mouseAction(new Mouse(Event));
			}

			@Override
			public void mouseDragged(MouseEvent Event) {
				Window.this.mouseAction(new Mouse(Event));
			}

			@Override
			public void mouseMoved(MouseEvent Event) {
				Window.this.mouseAction(new Mouse(Event));
			}

			@Override
			public void mouseClicked(MouseEvent Event) {
				Window.this.mouseAction(new Mouse(Event));
			}

			@Override
			public void mousePressed(MouseEvent Event) {
				Window.this.mouseAction(new Mouse(Event));
			}

			@Override
			public void mouseReleased(MouseEvent Event) {
				Window.this.mouseAction(new Mouse(Event));
			}

			@Override
			public void mouseEntered(MouseEvent Event) {
				Window.this.mouseAction(new Mouse(Event));
			}

			@Override
			public void mouseExited(MouseEvent Event) {
				Window.this.mouseAction(new Mouse(Event));
			}
		}
	}
}

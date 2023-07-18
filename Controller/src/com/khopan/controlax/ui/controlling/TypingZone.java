package com.khopan.controlax.ui.controlling;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JEditorPane;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.action.action.KeyboardAction;

public class TypingZone extends JEditorPane {
	private static final long serialVersionUID = 7150698209303170531L;

	private int width;
	private int height;
	private boolean focused;

	public TypingZone() {
		this.setCursor(Cursor.getDefaultCursor());
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent Event) {
				TypingZone.this.focused = true;
				TypingZone.this.repaint();
			}

			@Override
			public void focusLost(FocusEvent Event) {
				TypingZone.this.focused = false;
				TypingZone.this.repaint();
			}
		});

		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent Event) {

			}

			@Override
			public void keyPressed(KeyEvent Event) {
				if(Controlax.INSTANCE.window.controllingPanel.keyboardControlBox.isSelected()) {
					Controlax.INSTANCE.processor.sendAction(KeyboardAction.getKeyPressed(Event.getKeyCode()));
				}
			}

			@Override
			public void keyReleased(KeyEvent Event) {
				if(Controlax.INSTANCE.window.controllingPanel.keyboardControlBox.isSelected()) {
					Controlax.INSTANCE.processor.sendAction(KeyboardAction.getKeyReleased(Event.getKeyCode()));
				}
			}
		});
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
		super.paint(Graphics);
		Graphics2D Graphics2D = (Graphics2D) Graphics;
		Graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics2D.setColor(new Color(this.focused ? 0x008E00 : 0x828790));
		Graphics2D.fillRect(0, 0, this.width, this.height);
		int border = 1;
		Graphics2D.setColor(new Color(this.focused ? 0x93FF93 : 0xFFFFFF));
		Graphics2D.fillRect(border, border, this.width - border * 2, this.height - border * 2);
		Graphics2D.dispose();
	}
}

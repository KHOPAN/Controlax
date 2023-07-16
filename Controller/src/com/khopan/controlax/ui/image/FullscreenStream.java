package com.khopan.controlax.ui.image;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.khopan.controlax.Controlax;

public class FullscreenStream {
	private final JFrame frame;

	public FullscreenStream() {
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.frame.setUndecorated(true);
		this.frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent Event) {
				FullscreenStream.this.dispose();
			}
		});

		this.frame.setLayout(new BorderLayout());
	}

	public void show() {
		Controlax.INSTANCE.window.frame.dispose();
		this.frame.add(Controlax.INSTANCE.window.streamRenderer, BorderLayout.CENTER);
		this.frame.setVisible(true);
	}

	public void dispose() {
		if(this.frame.isVisible()) {
			this.frame.dispose();
			Controlax.INSTANCE.window.firstSubPanel.add(Controlax.INSTANCE.window.streamRenderer);
			Controlax.INSTANCE.window.frame.setVisible(true);
		}
	}
}

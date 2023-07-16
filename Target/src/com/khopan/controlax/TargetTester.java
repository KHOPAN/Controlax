package com.khopan.controlax;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.khopan.controlax.ui.color.ColorDisplay;

public class TargetTester { // MEANT TO SWITCH COLOR REALLY FAST UNLIKE ColorDisplay
	public static void test() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setCursor(ColorDisplay.BLANK_CURSOR);
		frame.setLayout(new BorderLayout());
		frame.add(new Panel(frame), BorderLayout.CENTER);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}

	private static class Panel extends Component {
		private static final long serialVersionUID = 6411085830716978146L;

		private Color color;
		private int width;
		private int height;

		private Panel(JFrame frame) {
			Random random = new Random();
			long time = System.currentTimeMillis();
			new Thread(() -> {
				while(true) {
					try {
						this.color = new Color(random.nextInt(0xFFFFFF + 1));
						this.repaint();

						if(System.currentTimeMillis() - time > 500) {
							frame.dispose();
						}

						Thread.sleep(1);
					} catch(Throwable Errors) {

					}
				}
			}).start();
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
			Graphics.setColor(this.color);
			Graphics.fillRect(0, 0, this.width, this.height);
		}
	}
}

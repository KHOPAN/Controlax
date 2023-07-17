package com.khopan.controlax;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ErrorEffect {
	private ErrorEffect() {}

	public static void start() {
		Random random = new Random();
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		new Thread(() -> {
			try {
				for(int i = 0; i < 50; i++) {
					new Thread(() -> {
						JOptionPane pane = new JOptionPane("Traceback (mot recent call last)\nFile \"c:\\users\\proj\\cx_Freeze-3.0.3\\initscripts\\console.py\", line 27, in ?\nFile \"src\\FretsOnFire.py\", line 79, in ?\nFile \"src\\GameEngine.py\", line 376, in run\nFile \"src\\Dialogs.py\", line 1153, in showMessage\nFile \"src\\Dialogs.py\", line 1043, in _runDialog\nFile \"src\\GameEngine.py\", line 366, in run\nSystemExit: 1", JOptionPane.ERROR_MESSAGE);
						JDialog dialog = pane.createDialog((JFrame) null, "cx_Freeze: Python error in main script");
						dialog.setLocation(random.nextInt(size.width - dialog.getWidth()), random.nextInt(size.height - dialog.getHeight()));
						dialog.setVisible(true);
					}).start();

					Thread.sleep(50);
				}
			} catch(Throwable Errors) {
				Errors.printStackTrace();
			}
		}).start();
	}
}

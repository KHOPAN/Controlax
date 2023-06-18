package com.khopan.controlax;

import java.awt.GridLayout;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class IPInputWindow {
	public final JFrame frame;
	public final JTextField textField;

	public IPInputWindow() {
		this.frame = new JFrame();
		this.frame.setTitle("Enter IP Address");
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLayout(new GridLayout(2, 1));
		this.textField = new JTextField();
		JButton button = new JButton();
		button.setText("Enter");
		button.setFocusable(false);
		button.addActionListener(Event -> {
			try {
				this.frame.dispose();
				String text = this.textField.getText();

				if(text.isEmpty()) {
					throw new InternalError();
				}

				InetAddress address = InetAddress.getByName(text);
				Controlax.INSTANCE.addressEntered(address);
			} catch(Throwable Errors) {
				this.frame.setVisible(true);
				this.textField.setText("");
			}
		});

		this.frame.add(this.textField);
		this.frame.add(button);
		this.frame.setSize(200, 100);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}
}

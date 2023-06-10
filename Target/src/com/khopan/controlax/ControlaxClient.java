package com.khopan.controlax;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.khopan.lazel.client.Client;

public class ControlaxClient {
	private boolean stop;

	private final Robot robot;

	public ControlaxClient() {
		try {
			this.robot = new Robot();
		} catch(Throwable Errors) {
			throw new InternalError(Errors);
		}

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent Event) {
				if(!ControlaxClient.this.stop) {
					frame.setVisible(true);
				}
			}

			@Override
			public void windowIconified(WindowEvent Event) {
				if(!ControlaxClient.this.stop) {
					frame.setState(Frame.NORMAL);
				}
			}
		});

		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent Event) {
				if(!ControlaxClient.this.stop) {
					frame.setLocationRelativeTo(null);
				}
			}
		});

		frame.setLayout(new GridLayout(2, 1));
		JTextField textField = new JTextField();
		JButton button = new JButton();
		button.setText("Enter");
		button.setFocusable(false);
		button.addActionListener(Event -> {
			try {
				frame.dispose();
				this.stop = true;
				InetAddress address = InetAddress.getByName(textField.getText());
				this.startClient(address);
			} catch(Throwable Errors) {
				this.stop = false;
				frame.setVisible(true);
				textField.setText("");
			}
		});

		frame.add(textField);
		frame.add(button);
		frame.setSize(200, 100);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private void startClient(InetAddress address) {
		Client client = new Client();
		client.port().set(2553);
		client.host().set(address.getHostAddress());
		client.serverConnectionListener().set(gateway -> {
			gateway.packetReceiver().set(packet -> {

			});
		});

		client.start();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			new ControlaxClient();
		} catch(Throwable Errors) {
			Errors.printStackTrace();
		}
	}
}

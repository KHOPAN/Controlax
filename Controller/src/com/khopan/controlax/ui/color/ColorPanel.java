package com.khopan.controlax.ui.color;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.ControlaxServer;
import com.khopan.lazel.Packet;
import com.khopan.lazel.config.BinaryConfigObject;

public class ColorPanel extends JPanel {
	private static final long serialVersionUID = 8481946450479918410L;

	public final JTextField colorInputField;
	public final JTextField transparencyInputField;
	public final JButton sendColorButton;
	public final JButton clearButton;
	public final JTextField movingRainbowRateInputField;
	public final JButton movingRainbowButton;
	public final JButton rainbowButton;

	public ColorPanel() {
		this.setBorder(new TitledBorder("Color"));
		this.setLayout(new GridLayout(7, 1));
		JPanel colorInputPanel = new JPanel();
		colorInputPanel.setLayout(new GridLayout(1, 2));
		JLabel colorInputLabel = new JLabel();
		colorInputLabel.setText("Color:");
		colorInputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		colorInputPanel.add(colorInputLabel);
		this.colorInputField = new JTextField();
		colorInputPanel.add(this.colorInputField);
		this.add(colorInputPanel);
		JPanel transparencyInputPanel = new JPanel();
		transparencyInputPanel.setLayout(new GridLayout(1, 2));
		JLabel transparencyInputLabel = new JLabel();
		transparencyInputLabel.setText("Transparency:");
		transparencyInputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		transparencyInputPanel.add(transparencyInputLabel);
		this.transparencyInputField = new JTextField();
		transparencyInputPanel.add(this.transparencyInputField);
		this.add(transparencyInputPanel);
		this.sendColorButton = new JButton();
		this.sendColorButton.setText("Send Color");
		this.sendColorButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 3);
			config.putInt("SubAction", 1);
			int color;

			try {
				color = Integer.parseInt(this.colorInputField.getText(), 16);

				if(color < 0x000000 || color > 0xFFFFFF) {
					throw new IllegalArgumentException();
				}
			} catch(Throwable Errors) {
				color = 0xFFFFFF;
			}

			this.colorInputField.setText(Integer.toString(color));
			float transparency;

			try {
				transparency = Float.parseFloat(this.transparencyInputField.getText());

				if(transparency < 0.0f || transparency > 1.0f) {
					throw new IllegalArgumentException();
				}
			} catch(Throwable Errors) {
				transparency = 1.0f;
			}

			this.transparencyInputField.setText(Float.toString(transparency));
			config.putInt("Color", color);
			config.putFloat("Transparency", transparency);
			ControlaxServer.INSTANCE.gateway.sendPacket(new Packet(config));
		});

		this.add(this.sendColorButton);
		this.clearButton = new JButton();
		this.clearButton.setText("Clear Color");
		this.clearButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 3);
			config.putInt("SubAction", 0);
			ControlaxServer.INSTANCE.gateway.sendPacket(new Packet(config));
		});

		this.add(this.clearButton);
		JPanel movingRainbowRateInputPanel = new JPanel();
		movingRainbowRateInputPanel.setLayout(new GridLayout(1, 2));
		JLabel movingRainbowRateInputLabel = new JLabel();
		movingRainbowRateInputLabel.setText("Rainbow Moving Rate:");
		movingRainbowRateInputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		movingRainbowRateInputPanel.add(movingRainbowRateInputLabel);
		this.movingRainbowRateInputField = new JTextField();
		movingRainbowRateInputPanel.add(this.movingRainbowRateInputField);
		this.add(movingRainbowRateInputPanel);
		this.movingRainbowButton = new JButton();
		this.movingRainbowButton.setText("Moving Rainbow");
		this.movingRainbowButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 3);
			config.putInt("SubAction", 2);
			config.putBoolean("Moving", true);
			float transparency;

			try {
				transparency = Float.parseFloat(this.transparencyInputField.getText());

				if(transparency < 0.0f || transparency > 1.0f) {
					throw new IllegalArgumentException();
				}
			} catch(Throwable Errors) {
				transparency = 1.0f;
			}

			this.transparencyInputField.setText(Float.toString(transparency));
			float rate;

			try {
				rate = Float.parseFloat(this.movingRainbowRateInputField.getText());
			} catch(Throwable Errors) {
				rate = 0.0001f;
			}

			this.movingRainbowRateInputField.setText(Float.toString(rate));
			config.putFloat("Transparency", transparency);
			config.putFloat("Rate", rate);
			ControlaxServer.INSTANCE.gateway.sendPacket(new Packet(config));
		});

		this.add(this.movingRainbowButton);
		this.rainbowButton = new JButton();
		this.rainbowButton.setText("Rainbow");
		this.rainbowButton.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 3);
			config.putInt("SubAction", 2);
			config.putBoolean("Moving", false);
			float transparency;

			try {
				transparency = Float.parseFloat(this.transparencyInputField.getText());

				if(transparency < 0.0f || transparency > 1.0f) {
					throw new IllegalArgumentException();
				}
			} catch(Throwable Errors) {
				transparency = 1.0f;
			}

			this.transparencyInputField.setText(Float.toString(transparency));
			config.putFloat("Transparency", transparency);
			config.putFloat("Rate", 0.0001f);
			ControlaxServer.INSTANCE.gateway.sendPacket(new Packet(config));
		});

		this.add(this.rainbowButton);
	}
}

package com.khopan.controlax.ui.color;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.controlax.action.action.ColorAction;
import com.khopan.controlax.action.action.ErrorEffectAction;
import com.khopan.controlax.action.action.TestTargetAction;

public class ColorPanel extends JPanel {
	private static final long serialVersionUID = 8481946450479918410L;

	public final JTextField colorInputField;
	public final JTextField transparencyInputField;
	public final JButton sendColorButton;
	public final JButton clearButton;
	public final JButton testTargetButton;
	public final JTextField movingRainbowRateInputField;
	public final JButton movingRainbowButton;
	public final JButton rainbowButton;
	public final JButton errorEffectButton;

	public ColorPanel() {
		this.setBorder(new TitledBorder("Color"));
		this.setLayout(new GridLayout(6, 1));
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
		JPanel movingRainbowRateInputPanel = new JPanel();
		movingRainbowRateInputPanel.setLayout(new GridLayout(1, 2));
		JLabel movingRainbowRateInputLabel = new JLabel();
		movingRainbowRateInputLabel.setText("Rainbow Moving Rate:");
		movingRainbowRateInputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		movingRainbowRateInputPanel.add(movingRainbowRateInputLabel);
		this.movingRainbowRateInputField = new JTextField();
		movingRainbowRateInputPanel.add(this.movingRainbowRateInputField);
		this.add(movingRainbowRateInputPanel);
		JPanel colorTargetPanel = new JPanel();
		colorTargetPanel.setLayout(new GridLayout(1, 3));
		this.sendColorButton = new JButton();
		this.sendColorButton.setText("Send Color");
		this.sendColorButton.addActionListener(Event -> Controlax.INSTANCE.processor.sendAction(ColorAction.getSetColor(this.color(), this.transparency())));
		colorTargetPanel.add(this.sendColorButton);
		this.clearButton = new JButton();
		this.clearButton.setText("Clear Color");
		this.clearButton.addActionListener(Event -> Controlax.INSTANCE.processor.sendAction(ColorAction.getClearColor()));
		colorTargetPanel.add(this.clearButton);
		this.testTargetButton = new JButton();
		this.testTargetButton.setText("Test Target");
		this.testTargetButton.addActionListener(Event -> Controlax.INSTANCE.processor.sendAction(TestTargetAction.getInstance()));
		colorTargetPanel.add(this.testTargetButton);
		this.add(colorTargetPanel);
		JPanel rainbowPanel = new JPanel();
		rainbowPanel.setLayout(new GridLayout(1, 2));
		this.movingRainbowButton = new JButton();
		this.movingRainbowButton.setText("Moving Rainbow");
		this.movingRainbowButton.addActionListener(Event -> Controlax.INSTANCE.processor.sendAction(ColorAction.getMovingRainbow(this.transparency(), this.movingRate())));
		rainbowPanel.add(this.movingRainbowButton);
		this.rainbowButton = new JButton();
		this.rainbowButton.setText("Rainbow");
		this.rainbowButton.addActionListener(Event -> Controlax.INSTANCE.processor.sendAction(ColorAction.getRainbow(this.transparency())));
		rainbowPanel.add(this.rainbowButton);
		this.add(rainbowPanel);
		this.errorEffectButton = new JButton();
		this.errorEffectButton.setText("Error Effect");
		this.errorEffectButton.addActionListener(Event -> Controlax.INSTANCE.processor.sendAction(ErrorEffectAction.getInstance()));
		this.add(this.errorEffectButton);
	}

	private int color() {
		int color;

		try {
			color = Integer.parseInt(this.colorInputField.getText(), 16);

			if(color < 0x000000 || color > 0xFFFFFF) {
				throw new IllegalArgumentException();
			}
		} catch(Throwable Errors) {
			color = 0xFFFFFF;
		}

		this.colorInputField.setText(String.format("%02x%02x%02x", (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF).toUpperCase());
		return color;
	}

	private double transparency() {
		double transparency;

		try {
			transparency = Double.parseDouble(this.transparencyInputField.getText());

			if(transparency < 0.0d || transparency > 1.0d) {
				throw new IllegalArgumentException();
			}
		} catch(Throwable Errors) {
			transparency = 1.0d;
		}

		this.transparencyInputField.setText(Double.toString(transparency));
		return transparency;
	}

	private double movingRate() {
		double movingRate;

		try {
			movingRate = Double.parseDouble(this.movingRainbowRateInputField.getText());
		} catch(Throwable Errors) {
			movingRate = 0.0001d;
		}

		this.movingRainbowRateInputField.setText(Double.toString(movingRate));
		return movingRate;
	}
}

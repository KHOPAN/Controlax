package com.khopan.controlax.ui.controlling;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.khopan.controlax.Controlax;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class ControllingPanel extends JPanel {
	private static final long serialVersionUID = -5323401463653633664L;

	public JCheckBox mouseControlBox;
	public JCheckBox keyboardControlBox;
	public TypingZone typingZone;

	public ControllingPanel() {
		this.setBorder(new TitledBorder("Controlling"));
		this.setLayout(new GridLayout(1, 2));
		JPanel mouseKeyboardPanel = new JPanel();
		mouseKeyboardPanel.setLayout(new GridLayout(2, 1));
		JPanel mousePanel = new JPanel();
		mousePanel.setBorder(new TitledBorder("Mouse"));
		mousePanel.setLayout(new GridLayout(2, 1));
		this.mouseControlBox = new JCheckBox();
		this.mouseControlBox.setText("Mouse Control");
		this.mouseControlBox.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 7);
			config.putInt("SubAction", 1);
			config.putBoolean("Control", this.mouseControlBox.isSelected());
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		});

		mousePanel.add(this.mouseControlBox);
		mouseKeyboardPanel.add(mousePanel);
		JPanel keyboardPanel = new JPanel();
		keyboardPanel.setBorder(new TitledBorder("Keyboard"));
		keyboardPanel.setLayout(new GridLayout(2, 1));
		this.keyboardControlBox = new JCheckBox();
		this.keyboardControlBox.setText("Keyboard Control");
		this.keyboardControlBox.addActionListener(Event -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 7);
			config.putInt("SubAction", 2);
			config.putBoolean("Control", this.keyboardControlBox.isSelected());
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		});

		keyboardPanel.add(this.keyboardControlBox);
		mouseKeyboardPanel.add(keyboardPanel);
		this.add(mouseKeyboardPanel);
		JPanel typingPanel = new JPanel();
		typingPanel.setBorder(new TitledBorder("Typing Zone"));
		typingPanel.setLayout(new BorderLayout());
		this.typingZone = new TypingZone();
		typingPanel.add(this.typingZone, BorderLayout.CENTER);
		this.add(typingPanel);
	}
}

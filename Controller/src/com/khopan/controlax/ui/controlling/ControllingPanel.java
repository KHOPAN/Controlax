package com.khopan.controlax.ui.controlling;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ControllingPanel extends JPanel {
	private static final long serialVersionUID = -5323401463653633664L;

	public JCheckBox mouseControlBox;
	public JCheckBox mouseMoveBox;
	public JCheckBox mousePressBox;
	public JCheckBox mouseReleaseBox;
	public JCheckBox mouseWheelMoveBox;
	public JCheckBox keyboardControlBox;
	public JCheckBox keyPressBox;
	public JCheckBox keyReleaseBox;

	public ControllingPanel() {
		this.setBorder(new TitledBorder("Controlling"));
		this.setLayout(new GridLayout(1, 2));
		JPanel mousePanel = new JPanel();
		mousePanel.setBorder(new TitledBorder("Mouse"));
		mousePanel.setLayout(new BoxLayout(mousePanel, BoxLayout.Y_AXIS));
		this.mouseControlBox = new JCheckBox();
		this.mouseControlBox.setText("Mouse Control");
		this.mouseControlBox.addChangeListener(Event -> this.updateMouseControl());
		mousePanel.add(this.mouseControlBox);
		this.mouseMoveBox = new JCheckBox();
		this.mouseMoveBox.setText("Mouse Move");
		this.mouseMoveBox.setSelected(true);
		mousePanel.add(this.mouseMoveBox);
		this.mousePressBox = new JCheckBox();
		this.mousePressBox.setText("Mouse Press");
		this.mousePressBox.setSelected(true);
		mousePanel.add(this.mousePressBox);
		this.mouseReleaseBox = new JCheckBox();
		this.mouseReleaseBox.setText("Mouse Release");
		this.mouseReleaseBox.setSelected(true);
		mousePanel.add(this.mouseReleaseBox);
		this.mouseWheelMoveBox = new JCheckBox();
		this.mouseWheelMoveBox.setText("Mouse Wheel Move");
		this.mouseWheelMoveBox.setSelected(true);
		mousePanel.add(this.mouseWheelMoveBox);
		this.add(mousePanel);
		this.updateMouseControl();
		JPanel keyboardPanel = new JPanel();
		keyboardPanel.setBorder(new TitledBorder("Keyboard"));
		keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.Y_AXIS));
		this.keyboardControlBox = new JCheckBox();
		this.keyboardControlBox.setText("Keyboard Control");
		this.keyboardControlBox.addChangeListener(Event -> this.updateKeyboardControl());
		keyboardPanel.add(this.keyboardControlBox);
		this.keyPressBox = new JCheckBox();
		this.keyPressBox.setText("Key Press");
		this.keyPressBox.setSelected(true);
		keyboardPanel.add(this.keyPressBox);
		this.keyReleaseBox = new JCheckBox();
		this.keyReleaseBox.setText("Key Release");
		this.keyReleaseBox.setSelected(true);
		keyboardPanel.add(this.keyReleaseBox);
		this.add(keyboardPanel);
		this.updateKeyboardControl();
	}

	private void updateMouseControl() {
		boolean enable = this.mouseControlBox.isSelected();
		this.mouseMoveBox.setEnabled(enable);
		this.mousePressBox.setEnabled(enable);
		this.mouseReleaseBox.setEnabled(enable);
		this.mouseWheelMoveBox.setEnabled(enable);
	}

	private void updateKeyboardControl() {
		boolean enable = this.keyboardControlBox.isSelected();
		this.keyPressBox.setEnabled(enable);
		this.keyReleaseBox.setEnabled(enable);
	}
}

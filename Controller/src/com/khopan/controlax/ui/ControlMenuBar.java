package com.khopan.controlax.ui;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class ControlMenuBar extends JMenuBar {
	private static final long serialVersionUID = 8255372125439919123L;

	/*public final JMenuItem testTargetItem;
	public final JMenuItem errorEffectItem;
	public final JMenuItem sleepItem;
	public final JMenuItem shutdownItem;
	public final JMenuItem restartItem;
	public final JMenuItem clearOutputItem;
	public final JMenuItem emergencyItem;
	public final JMenuItem openChatItem;
	public final JMenuItem closeChatItem;*/
	public final JCheckBoxMenuItem normalToggle;
	public final JCheckBoxMenuItem errorToggle;
	public final JCheckBoxMenuItem debugToggle;

	public ControlMenuBar() {
		/*JMenu colorMenu = new JMenu();
		colorMenu.setText("Color");
		this.testTargetItem = new JMenuItem();
		this.testTargetItem.setText("Test Target");
		colorMenu.add(this.testTargetItem);
		this.errorEffectItem = new JMenuItem();
		this.errorEffectItem.setText("Error Effect");
		colorMenu.add(this.errorEffectItem);
		this.add(colorMenu);
		JMenu commandMenu = new JMenu();
		commandMenu.setText("Command");
		JMenu powerMenu = new JMenu();
		powerMenu.setText("Power");
		this.sleepItem = new JMenuItem();
		this.sleepItem.setText("Sleep");
		powerMenu.add(this.sleepItem);
		this.shutdownItem = new JMenuItem();
		this.shutdownItem.setText("Shutdown");
		powerMenu.add(this.shutdownItem);
		this.restartItem = new JMenuItem();
		this.restartItem.setText("Restart");
		powerMenu.add(this.restartItem);
		commandMenu.add(powerMenu);
		this.clearOutputItem = new JMenuItem();
		this.clearOutputItem.setText("Clear Output Window");
		commandMenu.add(this.clearOutputItem);
		this.emergencyItem = new JMenuItem();
		this.emergencyItem.setText("Emergency Terminate");
		this.emergencyItem.setForeground(new Color(0xFF0000));
		commandMenu.add(this.emergencyItem);
		this.add(commandMenu);
		JMenu messageMenu = new JMenu();
		messageMenu.setText("Message");
		this.openChatItem = new JMenuItem();
		this.openChatItem.setText("Open Chat");
		messageMenu.add(this.openChatItem);
		this.closeChatItem = new JMenuItem();
		this.closeChatItem.setText("Close Chat");
		messageMenu.add(this.closeChatItem);
		this.add(messageMenu);*/
		JMenu debugMenu = new JMenu();
		debugMenu.setText("Debug");
		JMenu outputMenu = new JMenu();
		outputMenu.setText("Output");
		this.normalToggle = new JCheckBoxMenuItem();
		this.normalToggle.setText("Show Normal Output");
		this.normalToggle.setSelected(true);
		outputMenu.add(this.normalToggle);
		this.errorToggle = new JCheckBoxMenuItem();
		this.errorToggle.setText("Show Error Output");
		this.errorToggle.setSelected(true);
		outputMenu.add(this.errorToggle);
		this.debugToggle = new JCheckBoxMenuItem();
		this.debugToggle.setText("Show Debug Output");
		outputMenu.add(this.debugToggle);
		debugMenu.add(outputMenu);
		this.add(debugMenu);
	}
}

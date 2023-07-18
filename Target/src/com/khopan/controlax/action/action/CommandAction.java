package com.khopan.controlax.action.action;

import java.util.Map;

import com.khopan.controlax.action.Action;
import com.khopan.lazel.config.BinaryConfigObject;

public class CommandAction extends Action {
	private final String directory;
	private final String command;

	private CommandAction(String directory, String command) {
		if(command == null) {
			throw new NullPointerException("Command cannot be null");
		}

		this.directory = directory; 
		this.command = command;
	}

	private CommandAction(BinaryConfigObject config) {
		Map<String, Object> map = config.map();

		if(!map.containsKey("Directory")) {
			throw new IllegalArgumentException("Broken CommandAction object, key 'Directory' missing");
		}

		if(!map.containsKey("Command")) {
			throw new IllegalArgumentException("Broken CommandAction object, key 'Command' missing");
		}

		this.directory = config.getString("Directory");
		this.command = config.getString("Command");
	}

	public String getDirectory() {
		return this.directory;
	}

	public String getCommand() {
		return this.command;
	}

	@Override
	public void actionData(BinaryConfigObject config) {
		config.putString("Directory", this.directory);
		config.putString("Command", this.command);
	}

	public static CommandAction getInstance(String directory, String command) {
		return new CommandAction(directory, command);
	}
}

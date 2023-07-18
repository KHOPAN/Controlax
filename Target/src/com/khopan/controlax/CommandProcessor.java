package com.khopan.controlax;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.khopan.controlax.action.action.CommandAction;
import com.khopan.controlax.action.action.PowerAction;
import com.khopan.controlax.action.action.ResponseAction;

public class CommandProcessor {
	public static void command(CommandAction action) {
		String directory = action.getDirectory();
		String output;

		try {
			String resultCommand = "";

			if(directory != null && !directory.isEmpty()) {
				resultCommand += "cd " + directory + " && ";
			}

			resultCommand += action.getCommand();
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", resultCommand);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			output = "";
			String line;

			while(true) {
				line = reader.readLine();

				if(line == null) {
					break;
				}

				output += line + "\n";
			}
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Client: Error while executing command", Errors).printStackTrace(printWriter);
			output = stringWriter.toString();
		}

		Controlax.INSTANCE.processor.sendAction(ResponseAction.getInstance(output));
	}

	public static void power(PowerAction action) {
		int code = action.getAction();
		String command = "";

		if(code == PowerAction.ACTION_SLEEP) {
			command = "rundll32.exe powrprof.dll, SetSuspendState Sleep";
		} else if(code == PowerAction.ACTION_SHUTDOWN) {
			command = "shutdown -s -t 0";
		} else if(code == PowerAction.ACTION_RESTART) {
			command = "shutdown -r -t 0";
		}

		try {
			new ProcessBuilder("cmd.exe", "/c", command).start();
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Client: Error while executing command", Errors).printStackTrace(printWriter);
			Controlax.INSTANCE.processor.sendAction(ResponseAction.getInstance(stringWriter.toString()));
		}
	}
}

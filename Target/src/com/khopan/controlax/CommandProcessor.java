package com.khopan.controlax;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class CommandProcessor {
	public static void process(BinaryConfigObject config) {
		String directory = config.getString("Directory");
		String command = config.getString("Command");
		int identifierCode = config.getInt("IdentifierCode");
		String output;

		try {
			String resultCommand = "";

			if(directory != null && !directory.isEmpty()) {
				resultCommand += "cd " + directory + " && ";
			}

			resultCommand += command;
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

		BinaryConfigObject result = new BinaryConfigObject();
		result.putInt("Action", 1);
		result.putInt("IdentifierCode", identifierCode);
		result.putString("Result", output);
		Controlax.INSTANCE.sendPacket(new BinaryConfigPacket(result));
	}

	public static void processSystem(BinaryConfigObject config) {
		try {
			int action = config.getInt("SubAction");

			if(action == 1) {
				new ProcessBuilder("cmd.exe", "/c", "rundll32.exe powrprof.dll, SetSuspendState Sleep").start();
			} else if(action == 2) {
				new ProcessBuilder("cmd.exe", "/c", "shutdown -s -t 0").start();
			} else if(action == 3) {
				new ProcessBuilder("cmd.exe", "/c", "shutdown -r -t 0").start();
			}
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Client: Error while executing system action", Errors).printStackTrace(printWriter);
			BinaryConfigObject result = new BinaryConfigObject();
			result.putInt("Action", 2);
			result.putString("Error", stringWriter.toString());
			Controlax.INSTANCE.sendPacket(new BinaryConfigPacket(result));
		}
	}
}

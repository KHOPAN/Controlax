package com.khopan.controlax.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;

import javax.swing.UIManager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khopan.controlax.Controlax;

public class AutoUpdate {
	private static boolean Initialized;

	public static final File USER_HOME = new File(System.getProperty("user.home"));
	public static final File DOWNLOAD_FILE = new File(AutoUpdate.USER_HOME, "file.exe");

	private AutoUpdate() throws Throwable {
		URLConnection connection = new URI("https://raw.githubusercontent.com/KHOPAN/Controlax/main/Controlax/controlax-data.json").toURL().openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String response = "";
		String line;

		while((line = reader.readLine()) != null) {
			response += line + "\n";
		}

		JsonNode parent = new ObjectMapper().readTree(response);
		int version = parent.get("latest-version").asInt();
		String downloadLink = parent.get("download-url").textValue();

		if(!AutoUpdate.DOWNLOAD_FILE.exists()) {
			this.downloadLatestVersion(downloadLink);
			this.autoRun();
		}

		if(version > Controlax.VERSION) {
			this.execute();
			System.exit(0);
		} else {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			Controlax.INSTANCE = new Controlax();
		}
	}

	private void downloadLatestVersion(String link) throws Throwable {
		URLConnection connection = new URI(link).toURL().openConnection();
		byte[] data = connection.getInputStream().readAllBytes();
		FileOutputStream stream = new FileOutputStream(AutoUpdate.DOWNLOAD_FILE);
		stream.write(data);
		stream.close();
	}

	private void autoRun() throws Throwable {
		new ProcessBuilder("cmd.exe", "/c", "reg add \"HKLM\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\" /v Controlax /d \"" + AutoUpdate.DOWNLOAD_FILE.getAbsolutePath() + "\"").start();
	}

	private void execute() throws Throwable {
		new ProcessBuilder("cmd.exe", "/c", AutoUpdate.DOWNLOAD_FILE.getAbsolutePath()).start();
	}

	public static void initialize() {
		if(!AutoUpdate.Initialized) {
			try {
				new AutoUpdate();
			} catch(Throwable Errors) {
				Errors.printStackTrace();
			}

			AutoUpdate.Initialized = true;
		}
	}
}

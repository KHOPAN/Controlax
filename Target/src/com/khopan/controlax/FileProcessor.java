package com.khopan.controlax;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.khopan.controlax.action.action.ResponseAction;
import com.khopan.controlax.packet.FilePacket;

public class FileProcessor {
	public static void process(FilePacket packet) {
		try {
			byte[] content = packet.getFileData();
			File destination = packet.getDestination();
			FileOutputStream stream = new FileOutputStream(destination);
			stream.write(content);
			stream.close();
			Desktop.getDesktop().open(destination);
		} catch(Throwable Errors) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			new InternalError("Client: Error while creating file", Errors).printStackTrace(printWriter);
			Controlax.INSTANCE.processor.sendAction(ResponseAction.getInstance(stringWriter.toString()));
		}
	}
}

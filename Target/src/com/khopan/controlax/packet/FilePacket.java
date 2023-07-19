package com.khopan.controlax.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import com.khopan.lazel.config.Converter;
import com.khopan.lazel.packet.Packet;

public class FilePacket extends Packet {
	private final byte[] fileData;
	private final File destination;

	public FilePacket(byte[] byteArray) {
		super(byteArray);

		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(this.byteArray);
			int length = Converter.byteToInt(stream.readNBytes(4));
			this.destination = new File(new String(stream.readNBytes(length)));
			this.fileData = stream.readAllBytes();
		} catch(Throwable Errors) {
			throw new InternalError("Error while reading the file data", Errors);
		}
	}

	public FilePacket(File file, File destination) {
		super(null);

		if(file == null) {
			throw new NullPointerException("File cannot be null");
		}

		if(!file.exists()) {
			throw new IllegalArgumentException("File does not exist");
		}

		if(destination == null) {
			destination = file;
		}

		this.destination = destination;

		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] data = fileInputStream.readAllBytes();
			this.fileData = data;
			fileInputStream.close();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] destinationPath = this.destination.getAbsolutePath().getBytes();
			stream.write(Converter.intToByte(destinationPath.length));
			stream.write(destinationPath);
			stream.write(this.fileData);
			this.byteArray = stream.toByteArray();
		} catch(Throwable Errors) {
			throw new InternalError("Error while reading the file content", Errors);
		}
	}

	public byte[] getFileData() {
		return this.fileData;
	}

	public File getDestination() {
		return this.destination;
	}
}

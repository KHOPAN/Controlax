package com.khopan.controlax.packet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.khopan.lazel.config.Converter;
import com.khopan.lazel.packet.Packet;

public class HeaderedImagePacket extends Packet {
	private final BufferedImage image;
	private final byte header;
	private final int frame;

	public HeaderedImagePacket(byte[] byteArray) {
		super(byteArray);

		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(this.byteArray);
			this.header = (byte) stream.read();
			this.frame = Converter.byteToInt(stream.readNBytes(4));
			this.image = ImageIO.read(stream);
		} catch(Throwable Errors) {
			throw new InternalError("Error while reading the image", Errors);
		}
	}

	public HeaderedImagePacket(BufferedImage image, byte header, int frame) {
		super(null);

		if(image == null) {
			throw new NullPointerException("Image cannot be null");
		}

		this.image = image;
		this.header = header;
		this.frame = frame;

		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			stream.write(this.header);
			stream.write(Converter.intToByte(this.frame));
			ImageIO.write(image, "png", stream);
			this.byteArray = stream.toByteArray();
		} catch(Throwable Errors) {
			throw new InternalError("Error while converting an image into byte array", Errors);
		}
	}

	public byte getHeader() {
		return this.header;
	}

	public int getFrame() {
		return this.frame;
	}

	public BufferedImage getImage() {
		return this.image;
	}
}

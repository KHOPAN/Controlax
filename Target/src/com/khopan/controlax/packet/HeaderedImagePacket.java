package com.khopan.controlax.packet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.khopan.lazel.packet.Packet;

public class HeaderedImagePacket extends Packet {
	private final BufferedImage image;
	private final byte header;

	public HeaderedImagePacket(byte[] byteArray) {
		super(byteArray);

		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(this.byteArray);
			this.header = (byte) stream.read();
			this.image = ImageIO.read(stream);
		} catch(Throwable Errors) {
			throw new InternalError("Error while reading the image", Errors);
		}
	}

	public HeaderedImagePacket(BufferedImage image, byte header) {
		super(null);

		if(image == null) {
			throw new NullPointerException("Image cannot be null");
		}

		this.image = image;
		this.header = header;

		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			stream.write(this.header);
			ImageIO.write(image, "png", stream);
			this.byteArray = stream.toByteArray();
		} catch(Throwable Errors) {
			throw new InternalError("Error while converting an image into byte array", Errors);
		}
	}

	public byte getHeader() {
		return this.header;
	}

	public BufferedImage getImage() {
		return this.image;
	}
}

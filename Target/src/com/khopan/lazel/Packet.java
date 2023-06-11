package com.khopan.lazel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import com.khopan.lazel.config.BinaryConfig;
import com.khopan.lazel.config.BinaryConfig.ExceptionHandler;
import com.khopan.lazel.config.BinaryConfigElement;
import com.khopan.lazel.config.BinaryConfigObject;

public class Packet {
	private byte[] data;

	public Packet(byte[] raw) {
		this.data = raw;
	}

	public Packet(BinaryConfigObject config) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		BinaryConfig.write(config, stream);
		this.data = stream.toByteArray();
	}

	public byte[] send() {
		return this.data;
	}

	public boolean isRawData() {
		ExceptionHandler handler = BinaryConfig.exceptionHandler().get();
		AtomicBoolean error = new AtomicBoolean();
		error.set(false);
		BinaryConfig.exceptionHandler().set(throwable -> {
			error.set(true);
		});

		BinaryConfig.read(new ByteArrayInputStream(this.data));
		BinaryConfig.exceptionHandler().set(handler);
		return error.get();
	}

	public byte[] getRawData() {
		return this.data;
	}

	public BinaryConfigObject getBinaryConfigObject() {
		if(this.isRawData()) {
			throw new IllegalArgumentException("The data is not a BinaryConfigObject");
		}

		BinaryConfigElement element = BinaryConfig.read(new ByteArrayInputStream(this.data));

		if(element instanceof BinaryConfigObject object) {
			return object;
		} else {
			throw new IllegalArgumentException("Only BinaryConfigObject is allowed");
		}
	}

	public static Packet receive(byte[] data) {
		return new Packet(data);
	}
}

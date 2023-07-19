package me.saharnooby.qoi.plugin;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.stream.ImageInputStream;

/**
 * Wraps an {@link ImageInputStream} into an {@link InputStream}.
 */
final class WrappedImageInputStream extends InputStream {

	private final ImageInputStream input;

	public WrappedImageInputStream(ImageInputStream input) {
		this.input = input;
	}

	@Override
	public int read() throws IOException {
		return this.input.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return this.input.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return this.input.read(b, off, len);
	}

}

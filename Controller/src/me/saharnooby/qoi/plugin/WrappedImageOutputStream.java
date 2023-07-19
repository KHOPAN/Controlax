package me.saharnooby.qoi.plugin;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.stream.ImageOutputStream;

/**
 * Wraps an {@link ImageOutputStream} into an {@link OutputStream}.
 */
final class WrappedImageOutputStream extends OutputStream {

	private final ImageOutputStream output;

	public WrappedImageOutputStream(ImageOutputStream output) {
		this.output = output;
	}

	@Override
	public void write(byte[] b) throws IOException {
		this.output.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		this.output.write(b, off, len);
	}

	@Override
	public void write(int b) throws IOException {
		this.output.write(b);
	}

}

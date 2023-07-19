package me.saharnooby.qoi;

import java.io.IOException;

/**
 * This exception is thrown when decoder detects invalid data in the input stream.
 */
@SuppressWarnings("serial")
public final class InvalidQOIStreamException extends IOException {
	InvalidQOIStreamException(String message) {
		super(message);
	}
}

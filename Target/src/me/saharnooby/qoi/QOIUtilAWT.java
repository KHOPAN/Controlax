package me.saharnooby.qoi;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import me.saharnooby.qoi.plugin.QOIImageReader;
import me.saharnooby.qoi.plugin.QOIImageWriter;

/**
 * Contains public API methods of the library.
 */
public final class QOIUtilAWT {

	/**
	 * Converts a {@link RenderedImage} into a QOI image. Image data may be copied.
	 * @param image Source image.
	 * @return Conversion result.
	 */
	public static QOIImage createFromRenderedImage(RenderedImage image) {
		return QOIImageWriter.createFromRenderedImage(image);
	}

	/**
	 * Converts a {@link BufferedImage} into a QOI image. Image data may be copied.
	 * @param image Source image.
	 * @return Conversion result.
	 */
	public static QOIImage createFromBufferedImage(BufferedImage image) {
		return QOIImageWriter.createFromRenderedImage(image);
	}

	/**
	 * Converts a QOI image into a {@link BufferedImage}. Image data is not copied.
	 * @param image Source image.
	 * @return Conversion result.
	 */
	public static BufferedImage convertToBufferedImage(QOIImage image) {
		return QOIImageReader.convertToBufferedImage(image);
	}

}

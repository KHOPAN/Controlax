package me.saharnooby.qoi.plugin;

import java.awt.Point;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageWriteParam;

final class ImageParamUtil {

	private static final Point ZERO = new Point(0, 0);

	static boolean isDefault(ImageReadParam param) {
		return param.getClass() == ImageReadParam.class &&
				param.getSourceRegion() == null &&
				param.getSourceXSubsampling() == 1 &&
				param.getSourceYSubsampling() == 1 &&
				param.getSubsamplingXOffset() == 0 &&
				param.getSubsamplingYOffset() == 0 &&
				param.getSourceBands() == null &&
				param.getDestinationType() == null &&
				param.getDestinationOffset().equals(ZERO) &&
				param.getDestination() == null &&
				param.getDestinationBands() == null;
	}

	static boolean isDefault(ImageWriteParam param) {
		return param.getClass() == ImageWriteParam.class &&
				param.getSourceRegion() == null &&
				param.getSourceXSubsampling() == 1 &&
				param.getSourceYSubsampling() == 1 &&
				param.getSubsamplingXOffset() == 0 &&
				param.getSubsamplingYOffset() == 0 &&
				param.getSourceBands() == null &&
				param.getDestinationType() == null &&
				param.getDestinationOffset().equals(ZERO);
	}

}

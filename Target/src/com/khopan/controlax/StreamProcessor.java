package com.khopan.controlax;

import com.khopan.controlax.packet.HeaderedImagePacket;

public class StreamProcessor {
	public static final StreamProcessor INSTANCE = new StreamProcessor();

	private final Thread processingThread;

	private long waitTime;
	private int frame;

	private StreamProcessor() {
		this.waitTime = Math.round(1000.0d / ((double) Controlax.getFramerate()));
		this.frame = 0;
		this.processingThread = new Thread(this :: process);
		this.processingThread.setPriority(7);
		this.processingThread.start();
	}

	private void process() {
		while(true) {
			try {
				HeaderedImagePacket packet = new HeaderedImagePacket(ImageProcessor.takeScreenshot(), ImageProcessor.STREAM_HEADER, this.frame);
				Controlax.INSTANCE.sendPacket(packet);
				Thread.sleep(this.waitTime);
			} catch(Throwable Errors) {

			}
		}
	}

	public static void load() {
		// Load the class only
	}
}

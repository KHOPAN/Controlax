package com.khopan.controlax.processor;

import com.khopan.controlax.Controlax;

public class StreamProcessor {
	public static final StreamProcessor INSTANCE = new StreamProcessor();

	private final Thread processingThread;

	private long waitTime;

	private StreamProcessor() {
		this.waitTime = Math.round(1000.0d / (((double) Controlax.getFramerate()) * 0.166666667d));
		this.processingThread = new Thread(this :: process);
		this.processingThread.start();
	}

	private void process() {
		while(true) {
			try {
				ImageProcessor.sendScreenshot(ImageProcessor.STREAM_HEADER);
				Thread.sleep(this.waitTime);
			} catch(Throwable Errors) {

			}
		}
	}

	public static void load() {
		// Load the class only
	}
}

package com.khopan.controlax;

public class StreamProcessor {
	public static final StreamProcessor INSTANCE = new StreamProcessor();

	private final Thread processingThread;

	private long waitTime;

	private StreamProcessor() {
		this.waitTime = Math.round(1000.0d / ((double) Controlax.getFramerate()));
		this.processingThread = new Thread(this :: process);
		this.processingThread.setPriority(7);
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

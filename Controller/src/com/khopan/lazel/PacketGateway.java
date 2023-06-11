package com.khopan.lazel;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.khopan.lazel.property.Property;
import com.khopan.lazel.property.SimpleProperty;

public class PacketGateway {
	private static int Receiver;

	private final Socket socket;
	private final OutputStream outputStream;
	private final InputStream inputStream;
	private PacketReceiver receiver;

	public PacketGateway(Socket socket) {
		try {
			this.socket = socket;
			this.outputStream = this.socket.getOutputStream();
			this.inputStream = this.socket.getInputStream();
			Thread thread = new Thread(() -> {
				while(true) {
					try {
						/*int available = this.inputStream.available();

						if(available != 0) {
							System.out.println("Available: " + available);
							System.out.println("Code:" + this.inputStream.read());
							byte[] data = this.inputStream.readNBytes(this.inputStream.available());
							Packet packet = Packet.receive(data);

							if(this.receiver != null) {
								this.receiver.receivePacket(packet);
							}
						}*/

						int available = this.inputStream.available();

						if(available != 0) {
							byte[] data = this.inputStream.readNBytes(available);
							Packet packet = Packet.receive(data);

							if(this.receiver != null) {
								this.receiver.receivePacket(packet);
							}
						}
					} catch(Throwable Errors) {
						throw new InternalError("Error while receiving packet", Errors);
					}
				}
			});

			thread.setPriority(6);
			PacketGateway.Receiver++;
			thread.setName("Packet Gateway Receiver #" + PacketGateway.Receiver);
			thread.start();
		} catch(Throwable Errors) {
			throw new InternalError("Error while initializing PacketGateway", Errors);
		}
	}

	public void sendPacket(Packet packet) {
		if(packet == null) {
			throw new NullPointerException("Packet cannot be null");
		}

		try {
			/*byte[] data = packet.send();
			System.out.println("Original: " + data.length);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			int remaining = data.length;

			while(remaining > 0) {
				stream.write(0x1E);
				int wrote = remaining > 65535 ? 65535 : remaining;
				stream.write(data, data.length - remaining, wrote);
				remaining -= wrote;
			}

			byte[] output = stream.toByteArray();
			System.out.println("Added: " + output.length);
			this.outputStream.write(output);*/
			this.outputStream.write(packet.send());
		} catch(Throwable Errors) {
			throw new InternalError("Error while sending a packet", Errors);
		}
	}

	public Property<PacketReceiver, PacketGateway> packetReceiver() {
		return new SimpleProperty<PacketReceiver, PacketGateway>(() -> this.receiver, receiver -> this.receiver = receiver, this).nullable();
	}
}

package com.khopan.lazel;

import com.khopan.lazel.packet.Packet;

@FunctionalInterface
public interface PacketReceiver {
	public void receivePacket(Packet packet);
}

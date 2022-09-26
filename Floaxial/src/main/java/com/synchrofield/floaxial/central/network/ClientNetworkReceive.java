package com.synchrofield.floaxial.central.network;

// receive packet on client
public interface ClientNetworkReceive {

	public void receive(ReloadPacket packet);

	public void receive(GameResetPacket packet);

	public void receive(DropPacket packet);

	public void receive(StatisticsPacket packet);
}

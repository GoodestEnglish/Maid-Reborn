package rip.diamond.maid.redis.messaging;

public interface IPacketHandler {

    String CHANNEL = "Packet:All";

    void connectToServer();

    void send(Packet packet);

}

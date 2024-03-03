package rip.diamond.maid.redis.messaging;

public interface IPacketHandler {

    String CHANNEL = "Packet:All";

    IPacketHandler connectToServer();

    void send(Packet packet);

}

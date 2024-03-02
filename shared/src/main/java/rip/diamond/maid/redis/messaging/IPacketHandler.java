package rip.diamond.maid.redis.messaging;

public interface IPacketHandler {

    void connectToServer();

    void send(Packet packet);

}

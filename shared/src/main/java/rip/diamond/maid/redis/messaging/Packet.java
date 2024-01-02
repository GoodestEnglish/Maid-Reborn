package rip.diamond.maid.redis.messaging;

public interface Packet {

    String getFrom();

    String getTo();

    void onReceive();
}

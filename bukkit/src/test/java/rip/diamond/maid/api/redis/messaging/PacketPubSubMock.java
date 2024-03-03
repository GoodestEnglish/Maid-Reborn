package rip.diamond.maid.api.redis.messaging;

import redis.clients.jedis.JedisPubSub;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.json.GsonProvider;

public final class PacketPubSubMock extends JedisPubSub {

    @Override
    public void onMessage(final String channel, final String message) {
        int packetMessageSplit = message.indexOf("||");
        String packetClassStr = message.substring(0, packetMessageSplit);
        String messageJson = message.substring(packetMessageSplit + "||".length());
        Class<?> packetClass;
        try {
            packetClass = Class.forName(packetClassStr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Packet packet = (Packet) GsonProvider.GSON.fromJson(messageJson, packetClass);

        try {
            packet.onReceive();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

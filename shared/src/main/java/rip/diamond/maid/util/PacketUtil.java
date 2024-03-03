package rip.diamond.maid.util;

import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.json.GsonProvider;

public class PacketUtil {

    /**
     * Send a packet to redis pubsub.
     * Only use this function if the class doesn't need to be unit test.
     *
     * @param packet The packet which need to be sent
     */
    public static void send(Packet packet) {
        MaidAPI.INSTANCE.getPacketHandler().send(packet);
    }

    public static String encode(Packet packet) {
        return packet.getClass().getName() + "||" + GsonProvider.GSON.toJson(packet);
    }

}

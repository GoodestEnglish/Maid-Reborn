package rip.diamond.maid.redis.messaging;

import lombok.experimental.UtilityClass;
import redis.clients.jedis.Jedis;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.concurrent.CompletableFuture;

@UtilityClass
public final class PacketHandler {
    public static final MaidAPI api = MaidAPI.INSTANCE;

    public static void init() {
        connectToServer();
    }

    public static void connectToServer() {
        new Thread(() -> {
            try (Jedis jedis = api.getJedis().getResource()) {
                if (api.getRedisCredentials().isAuth()) {
                    jedis.auth(api.getRedisCredentials().getPassword());
                }

                PacketPubSub pubSub = new PacketPubSub();
                String channel = "Packet:All";
                jedis.subscribe(pubSub, channel);
            }
        }, "Practice - Packet Subscribe Thread").start();
    }

    public static void send(Packet packet) {
        CompletableFuture.runAsync(() -> api.runRedisCommand((jedis) -> {
            String encodedPacket = packet.getClass().getName() + "||" + GsonProvider.GSON.toJson(packet);
            return jedis.publish("Packet:All", encodedPacket);
        }), api.getJedisExecutor());
    }
}

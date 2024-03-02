package rip.diamond.maid.redis.messaging;

import lombok.RequiredArgsConstructor;
import redis.clients.jedis.Jedis;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.concurrent.CompletableFuture;

public record PacketHandlerAdapter(MaidAPI api) implements IPacketHandler {

    @Override
    public void connectToServer() {
        new Thread(() -> {
            try (Jedis jedis = api.getJedisPool().getResource()) {
                if (api.getRedisCredentials().isAuth()) {
                    jedis.auth(api.getRedisCredentials().getPassword());
                }

                PacketPubSub pubSub = new PacketPubSub();
                String channel = "Packet:All";
                jedis.subscribe(pubSub, channel);
            }
        }, "Maid - Packet Subscribe Thread").start();
    }

    @Override
    public void send(Packet packet) {
        CompletableFuture.runAsync(() -> api.runRedisCommand((jedis) -> {
            String encodedPacket = packet.getClass().getName() + "||" + GsonProvider.GSON.toJson(packet);
            return jedis.publish("Packet:All", encodedPacket);
        }), api.getJedisExecutor());
    }
}

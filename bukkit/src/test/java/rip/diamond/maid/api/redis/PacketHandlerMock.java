package rip.diamond.maid.api.redis;

import com.github.fppt.jedismock.RedisServer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;
import rip.diamond.maid.MaidAPIMock;
import rip.diamond.maid.redis.messaging.IPacketHandler;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.redis.messaging.PacketPubSub;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class PacketHandlerMock implements IPacketHandler {

    private final MaidAPIMock api;
    private final RedisServer server;

    @SneakyThrows
    @Override
    public void connectToServer() {
        new Thread(() -> {
            try (Jedis jedis = new Jedis(server.getHost(), server.getBindPort())) {
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

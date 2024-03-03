package rip.diamond.maid.api.redis;

import com.github.fppt.jedismock.RedisServer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;
import rip.diamond.maid.MaidAPIMock;
import rip.diamond.maid.api.redis.messaging.PacketPubSubMock;
import rip.diamond.maid.redis.messaging.IPacketHandler;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.json.GsonProvider;

@RequiredArgsConstructor
public class PacketHandlerMock implements IPacketHandler {

    private final MaidAPIMock api;

    @SneakyThrows
    @Override
    public void connectToServer() {
        new Thread(() -> {
            try (Jedis jedis = api.getJedis()) {
                PacketPubSubMock pubSub = new PacketPubSubMock();
                jedis.subscribe(pubSub, CHANNEL);
            }
        }, "Maid - Packet Subscribe Thread").start();
    }

    @Override
    public void send(Packet packet) {
        api.runRedisCommand((jedis) -> {
            String encodedPacket = packet.getClass().getName() + "||" + GsonProvider.GSON.toJson(packet);
            return jedis.publish(CHANNEL, encodedPacket);
        });
    }
}

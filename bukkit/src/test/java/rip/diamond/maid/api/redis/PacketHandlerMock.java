package rip.diamond.maid.api.redis;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import rip.diamond.maid.MaidAPIMock;
import rip.diamond.maid.redis.messaging.IPacketHandler;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.PacketUtil;
import rip.diamond.maid.util.json.GsonProvider;

@RequiredArgsConstructor
public class PacketHandlerMock implements IPacketHandler {

    private final MaidAPIMock api;

    @SneakyThrows
    @Override
    public IPacketHandler connectToServer() {
        //No action is required. Subscribe should only be tested when necessary
        return this;
    }

    @Override
    public void send(Packet packet) {
        api.runRedisCommand((jedis) -> jedis.publish(CHANNEL, PacketUtil.encode(packet)));
    }
}

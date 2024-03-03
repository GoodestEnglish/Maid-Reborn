package rip.diamond.maid;

import be.seeseemelk.mockbukkit.ServerMock;
import com.github.fppt.jedismock.RedisServer;
import lombok.Getter;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;
import rip.diamond.maid.api.redis.PacketHandlerMock;
import rip.diamond.maid.api.server.BukkitPlatformMock;
import rip.diamond.maid.api.server.IPlatform;
import rip.diamond.maid.redis.RedisCommand;
import rip.diamond.maid.redis.messaging.IPacketHandler;
import rip.diamond.maid.redis.messaging.Packet;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;

@Getter
public class MaidAPIMock implements IMaidAPI {

    private final ServerMock server;
    private final RedisServer redisServer;
    private final Executor jedisExecutor;
    private final Jedis jedis;
    private final IPacketHandler packetHandler;

    @SneakyThrows
    public MaidAPIMock(ServerMock server) {
        this.server = server;
        this.redisServer = RedisServer.newRedisServer().start();
        this.jedisExecutor = Executors.newSingleThreadExecutor();
        this.jedis = mock(Jedis.class);
        this.packetHandler = new PacketHandlerMock(this).connectToServer();
    }

    @Override
    public IPlatform getPlatform() {
        return new BukkitPlatformMock(server);
    }

    @Override
    public IPacketHandler getPacketHandler() {
        return packetHandler;
    }

    @Override
    public <T> T runRedisCommand(RedisCommand<T> redisCommand) {
        T result = null;
        try {
            result = redisCommand.execute(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            jedis.close();
        }
        return result;
    }

    public Queue<Packet> getSentPackets() {
        return ((PacketHandlerMock) packetHandler).getPackets();
    }
}

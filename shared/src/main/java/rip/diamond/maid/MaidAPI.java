package rip.diamond.maid;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import rip.diamond.maid.api.server.IPlatform;
import rip.diamond.maid.redis.RedisCommand;
import rip.diamond.maid.redis.RedisCredentials;
import rip.diamond.maid.redis.messaging.IPacketHandler;
import rip.diamond.maid.redis.messaging.PacketHandlerAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
public class MaidAPI implements IMaidAPI {

    public static MaidAPI INSTANCE;

    private final IPlatform platform;

    private RedisCredentials redisCredentials;
    private JedisPool jedisPool;
    private Executor jedisExecutor;
    private IPacketHandler packetHandler;

    public MaidAPI(IPlatform platform) {
        INSTANCE = this;

        this.platform = platform;
    }

    public void start(RedisCredentials redisCredentials) {
        this.redisCredentials = redisCredentials;
        this.jedisPool = redisCredentials.isAuth() ?
                new JedisPool(new JedisPoolConfig(), redisCredentials.getHostname(), redisCredentials.getPort(), 20_000, redisCredentials.getPassword()) :
                new JedisPool(new JedisPoolConfig(), redisCredentials.getHostname(), redisCredentials.getPort(), 20_000);
        this.jedisExecutor = Executors.newSingleThreadExecutor();
        this.packetHandler = new PacketHandlerAdapter(this);
    }

    public void stop() {
        this.jedisPool.close();
    }

    public <T> T runRedisCommand(RedisCommand<T> redisCommand) {
        Jedis jedis = this.jedisPool.getResource();
        if (redisCredentials.isAuth()) {
            jedis.auth(redisCredentials.getPassword());
        }

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

}

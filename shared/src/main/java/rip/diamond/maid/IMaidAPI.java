package rip.diamond.maid;

import redis.clients.jedis.JedisPool;
import rip.diamond.maid.api.server.IPlatform;
import rip.diamond.maid.redis.RedisCommand;
import rip.diamond.maid.redis.messaging.IPacketHandler;

public interface IMaidAPI {

    IPlatform getPlatform();

    IPacketHandler getPacketHandler();

    <T> T runRedisCommand(RedisCommand<T> redisCommand);

}

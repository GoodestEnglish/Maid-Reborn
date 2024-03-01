package rip.diamond.maid.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.IServer;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.ServerUpdatePacket;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Server implements IServer {

    private final String ID;
    private final long startupTime;
    private double[] tps;

    private boolean loaded;
    private boolean whiteListed;

    private List<GlobalUser> onlinePlayers;
    private int maxPlayers;

    private boolean chatMuted;
    private long chatDelay;

    private long lastTick;

    @Override
    public long getUpTime() {
        return System.currentTimeMillis() - startupTime;
    }

    @Override
    public void update() {
        tps = new double[]{Bukkit.getTPS()[0], Bukkit.getTPS()[1], Bukkit.getTPS()[2]};
        loaded = Maid.INSTANCE.getServerManager().isAllowJoin();
        whiteListed = Bukkit.hasWhitelist();
        onlinePlayers = Bukkit.getOnlinePlayers().stream().map(player -> GlobalUser.of(Maid.INSTANCE.getUserManager().getUserNow(player.getUniqueId()))).toList();
        maxPlayers = Bukkit.getMaxPlayers();
        chatMuted = Maid.INSTANCE.getChatManager().isMuted();
        chatDelay = Maid.INSTANCE.getChatManager().getDelay();
        lastTick = System.currentTimeMillis();

        PacketHandler.send(new ServerUpdatePacket(Maid.API.getPlatform().getServerID(), this));
    }
}

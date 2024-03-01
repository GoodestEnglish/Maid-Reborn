package rip.diamond.maid.redis.packets.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.Preconditions;

import java.util.List;
import java.util.UUID;

/**
 * A packet which send a message to a player across all bukkit servers.
 */
public class MessagePacket implements Packet {

    private final String from;
    private final UUID playerUUID;
    private final List<String> message;

    public MessagePacket(String from, UUID playerUUID, List<String> message) {
        this.from = from;
        this.playerUUID = playerUUID;
        this.message = message;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public String getTo() {
        return "server";
    }

    @Override
    public void onReceive() {
        Preconditions.checkArgument(Maid.API.getPlatform().getPlatform() == Platform.BUKKIT, getClass().getSimpleName() + " can only run in bukkit platform");

        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null && player.isOnline()) {
            Common.sendMessage(player, message);
        }
    }
}

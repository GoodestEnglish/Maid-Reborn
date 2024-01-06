package rip.diamond.maid.redis.packets.bukkit;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Preconditions;

import java.util.UUID;

@Getter
public class PermissionUpdatePacket implements Packet {

    private final String from;
    private UUID uuid;

    public PermissionUpdatePacket(String serverID) {
        this.from = serverID;
    }

    public PermissionUpdatePacket(String serverID, UUID uuid) {
        this.from = serverID;
        this.uuid = uuid;
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
        Preconditions.checkArgument(MaidAPI.INSTANCE.getPlatform().getPlatform() == Platform.BUKKIT, getClass().getSimpleName() + " can only run in bukkit platform");

        if (uuid == null) {
            Bukkit.getOnlinePlayers().forEach(Player::recalculatePermissions);
            return;
        }

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.recalculatePermissions();
        }
    }
}
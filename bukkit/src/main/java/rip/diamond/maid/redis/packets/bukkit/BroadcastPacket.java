package rip.diamond.maid.redis.packets.bukkit;

import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.Preconditions;

import java.util.List;

/**
 * A packet which send a broadcast to all bukkit servers.
 * <p>
 * If the player has the alert permission, they will be able to view it.
 */
public class BroadcastPacket implements Packet {

    private final String from;
    private final String permission;
    private final List<String> message;

    public BroadcastPacket(String from, String permission, List<String> message) {
        this.from = from;
        this.permission = permission;
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

        Common.broadcastPermissionMessage(permission, message);
    }
}

package rip.diamond.maid.redis.packets.bukkit;

import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Alert;
import rip.diamond.maid.util.Preconditions;

/**
 * A packet which send an alert to all bukkit servers.
 * <p>
 * If the player has the alert permission, they will be able to view it.
 */
public class AlertPacket implements Packet {

    private final String from;
    private final Alert alert;
    private final String message;

    public AlertPacket(String from, Alert alert, String... args) {
        this.from = from;
        this.alert = alert;
        this.message = alert.get(args);
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
        MaidAPI.INSTANCE.getPlatform().broadcastMessage(alert.getType().getPermission(), message);
    }
}

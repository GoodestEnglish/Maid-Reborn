package rip.diamond.maid.redis.packets.bukkit;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.AlertType;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Preconditions;

@RequiredArgsConstructor
public class AlertPacket implements Packet {

    private final String from;
    private final String message;
    private final AlertType alertType;

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
        MaidAPI.INSTANCE.getPlatform().broadcastMessage(alertType.getPermission(), message);
    }
}

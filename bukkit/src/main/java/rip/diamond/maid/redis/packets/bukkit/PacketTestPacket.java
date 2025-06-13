package rip.diamond.maid.redis.packets.bukkit;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Preconditions;

import java.util.UUID;

@RequiredArgsConstructor
public class PacketTestPacket implements Packet {
    private final String fromTo;
    private final UUID uuid;
    private final String message;

    @Override
    public String getFrom() {
        return fromTo;
    }

    @Override
    public String getTo() {
        return fromTo;
    }

    @Override
    public void onReceive() {
        Preconditions.checkArgument(Maid.API.getPlatform().getPlatform() == Platform.BUKKIT, getClass().getSimpleName() + " can only run in bukkit platform");
        Maid.API.getPlatform().sendMessage(uuid, null, message);
    }
}

package rip.diamond.maid.redis.packets.bukkit;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Preconditions;

import java.util.UUID;

@RequiredArgsConstructor
public class PacketTestPacket implements Packet {
    private final UUID uuid;
    private final String message;

    @Override
    public String getFrom() {
        return MaidAPI.INSTANCE.getPlatform().getServerID();
    }

    @Override
    public String getTo() {
        return MaidAPI.INSTANCE.getPlatform().getServerID();
    }

    @Override
    public void onReceive() {
        Preconditions.checkArgument(MaidAPI.INSTANCE.getPlatform().getPlatform() == Platform.BUKKIT, getClass().getSimpleName() + " can only run in bukkit platform");
        MaidAPI.INSTANCE.getPlatform().sendMessage(uuid, null, message);
    }
}

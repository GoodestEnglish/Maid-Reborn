package rip.diamond.maid.redis.packets;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.redis.messaging.Packet;

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
        MaidAPI.INSTANCE.getPlatform().sendMessage(uuid, null, message);
    }
}

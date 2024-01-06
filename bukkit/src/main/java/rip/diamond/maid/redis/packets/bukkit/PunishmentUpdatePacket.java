package rip.diamond.maid.redis.packets.bukkit;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.punishment.Punishment;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Preconditions;

@RequiredArgsConstructor
public class PunishmentUpdatePacket implements Packet {

    private final String from;
    private final Punishment punishment;

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

        //Exclude from the server which sent this packet. Because punishment is already updated in local.
        if (MaidAPI.INSTANCE.getPlatform().getServerID().equals(getFrom())) {
            return;
        }

        PunishmentManager manager = Maid.INSTANCE.getPunishmentManager();
        manager.getPunishments().removeIf(punishment_ -> punishment_.getUniqueID().equals(punishment.getUniqueID()));
        manager.getPunishments().add(punishment);
    }
}

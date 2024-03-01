package rip.diamond.maid.redis.packets.bukkit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.rank.Rank;
import rip.diamond.maid.rank.RankManager;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Preconditions;

@Getter
@RequiredArgsConstructor
public class RankUpdatePacket implements Packet {
    private final String from;
    private final Rank rank;
    private final boolean delete;

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

        RankManager manager = Maid.INSTANCE.getRankManager();

        //Exclude from the server which sent this packet. Because rank is already updated in local.
        if (Maid.API.getPlatform().getServerID().equals(getFrom())) {
            return;
        }

        if (delete) {
            manager.getRanks().remove(rank.getUniqueID());
        } else {
            manager.getRanks().put(rank.getUniqueID(), rank);
        }
    }
}

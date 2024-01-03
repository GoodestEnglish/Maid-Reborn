package rip.diamond.maid.redis.packets.bukkit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.rank.Rank;
import rip.diamond.maid.rank.RankManager;
import rip.diamond.maid.redis.messaging.Packet;

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
        return "all";
    }

    @Override
    public void onReceive() {
        RankManager manager = Maid.INSTANCE.getRankManager();

        if (MaidAPI.INSTANCE.getPlatform().getServerID().equals(getFrom())) {
            return;
        }

        if (delete) {
            manager.getRanks().remove(rank.getUniqueID());
        } else {
            manager.getRanks().put(rank.getUniqueID(), rank);
        }
    }
}

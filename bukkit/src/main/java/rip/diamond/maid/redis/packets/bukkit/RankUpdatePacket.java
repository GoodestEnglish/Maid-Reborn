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
    private final Rank rank;
    private final boolean delete;

    @Override
    public String getFrom() {
        return MaidAPI.INSTANCE.getPlatform().getServerID();
    }

    @Override
    public String getTo() {
        return "all";
    }

    @Override
    public void onReceive() {
        RankManager manager = Maid.INSTANCE.getRankManager();

        if (delete) {
            manager.getRanks().remove(rank.getUniqueID());
        } else {
            manager.getRanks().put(rank.getUniqueID(), rank);
        }
    }
}

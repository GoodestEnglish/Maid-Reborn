package rip.diamond.maid.util.recorder;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.MessagePacket;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.Preconditions;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class RankChangesRecorder {

    private final IRank oldRank;
    private IRank newRank;

    public void recordNewRank(IRank newRank) {
        this.newRank = newRank;
    }

    public void outputChanges(UUID playerUUID) {
        Preconditions.checkArgument(canOutput(), "Failed out changes in " + getClass().getSimpleName() + " for " + playerUUID.toString());

        if (oldRank == newRank) {
            return;
        }
        if (newRank.getPriority() < oldRank.getPriority()) {
            return;
        }

        PacketHandler.send(new MessagePacket(Maid.API.getPlatform().getServerID(), playerUUID, List.of(CC.GREEN + "你已從 " + oldRank.getDisplayName(true) + CC.RESET + CC.GREEN + " 升級到 " + newRank.getDisplayName(true))));
    }

    public boolean canOutput() {
        return oldRank != null && newRank != null;
    }
}

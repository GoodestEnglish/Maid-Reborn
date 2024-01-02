package rip.diamond.maid.redis.packets.bukkit;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.player.PlayerManager;
import rip.diamond.maid.player.User;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Preconditions;

@RequiredArgsConstructor
public class ProfileUpdatePacket implements Packet {

    private final User user;

    @Override
    public String getFrom() {
        return MaidAPI.INSTANCE.getPlatform().getServerID();
    }

    @Override
    public String getTo() {
        return "server";
    }

    @Override
    public void onReceive() {
        Preconditions.checkArgument(MaidAPI.INSTANCE.getPlatform().getPlatform() == Platform.BUKKIT, getClass().getSimpleName() + " can only run in bukkit platform");

        PlayerManager manager = Maid.INSTANCE.getPlayerManager();
        if (manager.getUsers().containsKey(user.getUniqueId())) {
            manager.getUsers().replace(user.getUniqueId(), user);
        }
    }
}

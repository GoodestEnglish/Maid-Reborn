package rip.diamond.maid.redis.packets.bukkit;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.player.User;
import rip.diamond.maid.player.UserManager;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Preconditions;

@RequiredArgsConstructor
public class ProfileUpdatePacket implements Packet {

    private final String from;
    private final User user;

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

        if (MaidAPI.INSTANCE.getPlatform().getServerID().equals(getFrom())) {
            return;
        }

        UserManager manager = Maid.INSTANCE.getUserManager();
        if (manager.getUsers().containsKey(user.getUniqueID())) {
            manager.getUsers().replace(user.getUniqueID(), user);
        }
    }
}

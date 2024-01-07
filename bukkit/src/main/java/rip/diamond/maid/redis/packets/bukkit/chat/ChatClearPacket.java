package rip.diamond.maid.redis.packets.bukkit.chat;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.Preconditions;

@RequiredArgsConstructor
public class ChatClearPacket implements Packet {

    private final String from;
    private final IUser user;

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

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < 100; i++) {
                Common.sendMessage(player, " ");
            }
            Common.sendMessage(player, CC.GREEN + "聊天室已被 " + user.getSimpleDisplayName(false) + CC.GREEN + " 清空");
        }
    }
}

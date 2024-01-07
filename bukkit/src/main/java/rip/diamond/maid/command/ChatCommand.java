package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.chat.ChatClearPacket;
import rip.diamond.maid.redis.packets.bukkit.chat.ChatMutePacket;
import rip.diamond.maid.redis.packets.bukkit.chat.ChatSlowPacket;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.extend.MaidCommand;

@Require(MaidPermission.CHAT)
public class ChatCommand extends MaidCommand {

    @Command(name = "clear", desc = "清空聊天室")
    public void clear(@Sender Player sender) {
        IUser user = plugin.getUserManager().getUser(sender.getUniqueId()).join();
        PacketHandler.send(new ChatClearPacket(MaidAPI.INSTANCE.getPlatform().getServerID(), user));
    }

    @Command(name = "mute", desc = "切換聊天室狀態")
    public void mute(@Sender Player sender) {
        IUser user = plugin.getUserManager().getUser(sender.getUniqueId()).join();
        boolean muted = plugin.getChatManager().isMuted();

        plugin.getChatManager().setMuted(!muted);
        PacketHandler.send(new ChatMutePacket(MaidAPI.INSTANCE.getPlatform().getServerID(), user, !muted));
    }

    @Command(name = "delay", desc = "切換聊天室狀態")
    public void delay(@Sender Player sender, int delay) {
        IUser user = plugin.getUserManager().getUser(sender.getUniqueId()).join();

        plugin.getChatManager().setDelay(delay);
        PacketHandler.send(new ChatSlowPacket(MaidAPI.INSTANCE.getPlatform().getServerID(), user, delay));
    }

}

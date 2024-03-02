package rip.diamond.maid.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.Maid;
import rip.diamond.maid.util.MaidPermission;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.player.User;
import rip.diamond.maid.redis.packets.bukkit.chat.ChatClearPacket;
import rip.diamond.maid.redis.packets.bukkit.chat.ChatMutePacket;
import rip.diamond.maid.redis.packets.bukkit.chat.ChatSlowPacket;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.extend.MaidCommand;

@RequiredArgsConstructor
@Require(MaidPermission.CHAT)
public class ChatCommand extends MaidCommand {

    private final IMaidAPI api;

    @Command(name = "clear", desc = "清空聊天室")
    public void clear(@Sender Player sender) {
        IUser user = plugin.getUserManager().getUserNow(sender.getUniqueId());
        api.getPacketHandler().send(new ChatClearPacket(Maid.API.getPlatform().getServerID(), (User) user));
    }

    @Command(name = "mute", desc = "切換聊天室狀態")
    public void mute(@Sender Player sender) {
        IUser user = plugin.getUserManager().getUserNow(sender.getUniqueId());
        boolean muted = plugin.getChatManager().isMuted();

        plugin.getChatManager().setMuted(!muted);
        api.getPacketHandler().send(new ChatMutePacket(Maid.API.getPlatform().getServerID(), (User) user, !muted));
    }

    @Command(name = "delay", desc = "切換聊天室狀態")
    public void delay(@Sender Player sender, int delay) {
        IUser user = plugin.getUserManager().getUserNow(sender.getUniqueId());

        plugin.getChatManager().setDelay(delay);
        api.getPacketHandler().send(new ChatSlowPacket(Maid.API.getPlatform().getServerID(), (User) user, delay));
    }

}

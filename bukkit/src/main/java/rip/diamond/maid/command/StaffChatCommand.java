package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.util.MaidPermission;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.chat.ChatRoomType;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.chat.StaffMessagePacket;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.command.annotation.Text;
import rip.diamond.maid.util.extend.MaidCommand;

@Require(MaidPermission.STAFFCHAT)
public class StaffChatCommand extends MaidCommand {

    @Command(name = "", desc = "發送訊息到工作人員聊天室")
    public void root(@Sender Player sender, @Text String message) {
        IUser user = plugin.getUserManager().getUserNow(sender.getUniqueId());
        GlobalUser globalSender = GlobalUser.of(user);

        PacketHandler.send(new StaffMessagePacket(Maid.API.getPlatform().getServerID(), globalSender, message));
    }

    @Command(name = "toggle", desc = "切換工作人員聊天室狀態")
    public void toggle(@Sender Player sender) {
        IUser user = plugin.getUserManager().getUserNow(sender.getUniqueId());

        if (user.getChatRoom().getType() == ChatRoomType.STAFF) {
            user.getChatRoom().setType(ChatRoomType.PUBLIC);
            Common.sendMessage(sender, CC.GREEN + "聊天室已被切換到: 公眾聊天室");
        } else {
            user.getChatRoom().setType(ChatRoomType.STAFF);
            Common.sendMessage(sender, CC.GREEN + "聊天室已被切換到: 工作人員聊天室");
        }

        plugin.getUserManager().saveUser(user);
    }
}

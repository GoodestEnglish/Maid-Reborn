package rip.diamond.maid.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.server.IGlobalUser;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.config.ChatConfig;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.chat.DirectMessagePacket;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.extend.MaidManager;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChatManager extends MaidManager {

    private final ChatConfig chatConfig;

    public void setMuted(boolean muted) {
        this.chatConfig.setChatMuted(muted);
    }

    public boolean isMuted() {
        return this.chatConfig.isChatMuted();
    }

    public void setDelay(int delay) {
        this.chatConfig.setChatDelay(delay);
    }

    public int getDelay() {
        return this.chatConfig.getChatDelay();
    }


    public void sendDirectMessage(IUser user, IGlobalUser receiver, String message) {
        GlobalUser sender = GlobalUser.of(user);

        UUID messageTo = user.getChatRoom().getMessageTo();
        if (messageTo == null || !messageTo.equals(receiver.getUniqueID())) {
            user.getChatRoom().setMessageTo(receiver.getUniqueID());
            plugin.getUserManager().saveUser(user);
        }

        PacketHandler.send(new DirectMessagePacket(Maid.API.getPlatform().getServerID(), sender, (GlobalUser) receiver, message));
        Common.sendMessage(Bukkit.getPlayer(sender.getUniqueID()), CC.PINK + "➥ 給 " + receiver.getSimpleDisplayName() + CC.WHITE + ": " + CC.GRAY + message);
    }

}

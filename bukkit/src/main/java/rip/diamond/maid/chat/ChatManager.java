package rip.diamond.maid.chat;

import lombok.Getter;
import org.bukkit.Bukkit;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.config.Config;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.chat.DirectMessagePacket;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.extend.MaidManager;

@Getter
public class ChatManager extends MaidManager {
    private boolean muted;
    private int delay;

    public ChatManager() {
        this.muted = Config.CHAT_MUTED.toBoolean();
        this.delay = Config.CHAT_DELAY.toInteger();
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        Config.CHAT_MUTED.setValue(muted);
    }

    public void setDelay(int delay) {
        this.delay = delay;
        Config.CHAT_DELAY.setValue(delay);
    }

    public void sendDirectMessage(IUser user, GlobalUser receiver, String message) {
        GlobalUser sender = GlobalUser.of(user);

        user.getChatRoom().setMessageTo(receiver.getUniqueID());
        PacketHandler.send(new DirectMessagePacket(MaidAPI.INSTANCE.getPlatform().getServerID(), sender, receiver, message));
        Common.sendMessage(Bukkit.getPlayer(sender.getUniqueID()), CC.PINK + "➥ 給 " + receiver.getSimpleDisplayName() + CC.WHITE + ": " + CC.GRAY + message);
    }

}

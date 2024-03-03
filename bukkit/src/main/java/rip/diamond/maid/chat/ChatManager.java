package rip.diamond.maid.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.server.IGlobalUser;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.config.ChatConfig;
import rip.diamond.maid.player.UserManager;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.redis.packets.bukkit.chat.DirectMessagePacket;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChatManager {

    private final IMaidAPI api;
    private final UserManager userManager;
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

    public Packet sendDirectMessage(IUser user, IGlobalUser receiver, String message) {
        GlobalUser sender = GlobalUser.of(user, api);

        UUID messageTo = user.getChatRoom().getMessageTo();
        if (messageTo == null || !messageTo.equals(receiver.getUniqueID())) {
            user.getChatRoom().setMessageTo(receiver.getUniqueID());
            userManager.saveUser(user);
        }

        Packet packet = new DirectMessagePacket(api.getPlatform().getServerID(), sender, (GlobalUser) receiver, message);
        api.getPacketHandler().send(packet);

        Common.sendMessage(Bukkit.getPlayer(sender.getUniqueID()), CC.PINK + "➥ 給 " + receiver.getSimpleDisplayName() + CC.WHITE + ": " + CC.GRAY + message);

        return packet;
    }

}

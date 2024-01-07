package rip.diamond.maid.redis.packets.bukkit.chat;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.Preconditions;

@RequiredArgsConstructor
public class ChatMutePacket implements Packet {

    private final String from;
    private final IUser user;
    private final boolean muted;

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

        Maid.INSTANCE.getChatManager().setMuted(muted);
        Common.broadcastMessage(CC.GREEN + "聊天室狀態已被 " + user.getSimpleDisplayName(false) + CC.GREEN + " 切換到: " + (muted ? "關閉" : "開啟"));
    }
}

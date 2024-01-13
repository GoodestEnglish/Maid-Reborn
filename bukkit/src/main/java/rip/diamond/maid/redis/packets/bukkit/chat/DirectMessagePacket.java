package rip.diamond.maid.redis.packets.bukkit.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;

@Getter
@RequiredArgsConstructor
public class DirectMessagePacket implements Packet {

    private final String from;
    private final GlobalUser sender;
    private final GlobalUser receiver;
    private final String message;

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
        Player player = Bukkit.getPlayer(receiver.getUniqueID());
        if (player == null) {
            return;
        }
        Common.sendMessage(player, CC.LIGHT_PINK + "➥ 從 " + sender.getSimpleDisplayName() + ": " + CC.GRAY + message);
    }
}

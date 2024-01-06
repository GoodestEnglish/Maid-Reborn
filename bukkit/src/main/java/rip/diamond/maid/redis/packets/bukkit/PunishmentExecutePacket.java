package rip.diamond.maid.redis.packets.bukkit;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.punishment.Punishment;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.Preconditions;
import rip.diamond.maid.util.Tasks;

import java.util.List;

@RequiredArgsConstructor
public class PunishmentExecutePacket implements Packet {

    private final String from;
    private final Punishment punishment;

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

        Player player = Bukkit.getPlayer(punishment.getUser());
        if (player == null) {
            return;
        }

        List<String> message = Maid.INSTANCE.getPunishmentManager().getPunishmentMessage(punishment);

        switch (punishment.getType()) {
            case WARN, MUTE -> Common.sendMessage(player, message);
            case KICK, BAN, IP_BAN -> Tasks.run(() -> player.kick(Common.text(StringUtils.join(message, "\n"))));
        }
    }
}

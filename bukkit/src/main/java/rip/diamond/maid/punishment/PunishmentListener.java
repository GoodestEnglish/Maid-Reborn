package rip.diamond.maid.punishment;

import com.google.common.collect.ImmutableList;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.BroadcastPacket;
import rip.diamond.maid.util.Alert;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.extend.MaidListener;

import java.util.List;

public class PunishmentListener extends MaidListener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginPunishment(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        // TODO: 1/3/2024
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();
        List<IPunishment> punishments = user.getActivePunishments(List.of(IPunishment.PunishmentType.BAN, IPunishment.PunishmentType.IP_BAN));

        if (punishments.isEmpty()) {
            return;
        }

        IPunishment punishment = punishments.get(0);
        String message = StringUtils.join(plugin.getPunishmentManager().getPunishmentMessage(punishment), "\n");
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Common.text(message));

        String durationReadable = punishment.getDuration() == -1 ? "永久" : TimeUtil.formatDuration(punishment.getIssuedAt() + punishment.getDuration() - System.currentTimeMillis());
        Alert alert = Alert.LOGIN_FAILED_BANNED;
        PacketHandler.send(new BroadcastPacket(Maid.API.getPlatform().getServerID(), alert.getType().getPermission(), ImmutableList.of(alert.get(user.getSimpleDisplayName(false), "(" + durationReadable + ")"))));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatPunishment(AsyncChatEvent event) {
        Player player = event.getPlayer();
        IUser user = plugin.getUserManager().getUserNow(player.getUniqueId());
        List<IPunishment> punishments = user.getActivePunishments(List.of(IPunishment.PunishmentType.MUTE));

        if (punishments.isEmpty()) {
            return;
        }

        IPunishment punishment = punishments.get(0);
        List<String> messages = plugin.getPunishmentManager().getPunishmentMessage(punishment);

        event.setCancelled(true);
        Common.sendMessage(player, messages);

        Alert alert = Alert.CHAT_FAILED_MUTED;
        PacketHandler.send(new BroadcastPacket(Maid.API.getPlatform().getServerID(), alert.getType().getPermission(), ImmutableList.of(alert.get(user.getSimpleDisplayName(false), "(" + GsonComponentSerializer.gson().serialize(event.message()) + ")"))));
    }

}

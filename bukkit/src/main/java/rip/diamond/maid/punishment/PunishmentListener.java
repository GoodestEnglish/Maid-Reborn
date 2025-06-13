package rip.diamond.maid.punishment;

import com.google.common.collect.ImmutableList;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.player.UserManager;
import rip.diamond.maid.redis.packets.bukkit.BroadcastPacket;
import rip.diamond.maid.util.Alert;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.TimeUtil;

import java.util.List;

@RequiredArgsConstructor
public class PunishmentListener implements Listener {

    private final IMaidAPI api;
    private final UserManager userManager;
    private final PunishmentManager punishmentManager;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginPunishment(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        IUser user = userManager.getUserNow(player.getUniqueId());
        List<IPunishment> punishments = user.getActivePunishments(List.of(IPunishment.PunishmentType.BAN, IPunishment.PunishmentType.IP_BAN));

        if (punishments.isEmpty()) {
            return;
        }

        IPunishment punishment = punishments.get(0);
        String message = StringUtils.join(punishmentManager.getPunishmentMessage(punishment), "\n");
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Common.text(message));

        String durationReadable = punishment.getDuration() == -1 ? "永久" : TimeUtil.formatDuration(punishment.getIssuedAt() + punishment.getDuration() - System.currentTimeMillis());
        Alert alert = Alert.LOGIN_FAILED_BANNED;
        api.getPacketHandler().send(new BroadcastPacket(Maid.API.getPlatform().getServerID(), alert.getType().getPermission(), ImmutableList.of(alert.get(user.getSimpleDisplayName(false), "(" + durationReadable + ")"))));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatPunishment(AsyncChatEvent event) {
        Player player = event.getPlayer();
        IUser user = userManager.getUserNow(player.getUniqueId());
        List<IPunishment> punishments = user.getActivePunishments(List.of(IPunishment.PunishmentType.MUTE));

        if (punishments.isEmpty()) {
            return;
        }

        IPunishment punishment = punishments.get(0);
        List<String> messages = punishmentManager.getPunishmentMessage(punishment);

        event.setCancelled(true);
        Common.sendMessage(player, messages);

        Alert alert = Alert.CHAT_FAILED_MUTED;
        api.getPacketHandler().send(new BroadcastPacket(Maid.API.getPlatform().getServerID(), alert.getType().getPermission(), ImmutableList.of(alert.get(user.getSimpleDisplayName(false), "(" + GsonComponentSerializer.gson().serialize(event.message()) + ")"))));
    }

}

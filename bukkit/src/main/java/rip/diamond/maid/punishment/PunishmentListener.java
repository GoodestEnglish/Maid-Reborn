package rip.diamond.maid.punishment;

import com.google.common.collect.ImmutableList;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.extend.MaidListener;

import java.util.List;

public class PunishmentListener extends MaidListener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginPunishment(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();
        List<IPunishment> punishments = user.getActivePunishments(List.of(IPunishment.PunishmentType.BAN, IPunishment.PunishmentType.IP_BAN));

        if (punishments.isEmpty()) {
            return;
        }

        IPunishment punishment = punishments.get(0);
        String message = StringUtils.join(plugin.getPunishmentManager().getPunishmentMessage(punishment), "\n");
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Common.text(message));
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();
        List<IPunishment> punishments = user.getActivePunishments(List.of(IPunishment.PunishmentType.MUTE));

        if (punishments.isEmpty()) {
            return;
        }

        IPunishment punishment = punishments.get(0);
        List<String> messages = plugin.getPunishmentManager().getPunishmentMessage(punishment);

        event.setCancelled(true);
        Common.sendMessage(player, messages);
    }

}

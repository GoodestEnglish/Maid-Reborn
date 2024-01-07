package rip.diamond.maid.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.extend.MaidListener;

public class ChatListener extends MaidListener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatFormat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();
        IRank rank = user.getRank();

        ChatRenderer renderer = new ChatRenderer() {
            @Override
            public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
                return Common.text((viewer instanceof ConsoleCommandSender && user.getDisguise() != null ? "(真實名稱為 " + user.getRealName() + ") " : "") + user.getDisplayName(true) + CC.GRAY + ":" + CC.RESET + " ").append(message.color(TextColor.fromHexString(rank.getChatColor())));
            }
        };
        event.renderer(renderer);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChatMute(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getChatManager().isMuted()) {
            event.setCancelled(true);
            Common.sendMessage(player, CC.RED + "聊天室暫時被關閉了, 你暫時無法再聊天室說話");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChatDelay(AsyncChatEvent event) {
        Player player = event.getPlayer();
        int delay = plugin.getChatManager().getDelay();

        if (delay <= 0) {
            return;
        }

        if (player.hasMetadata("chat-delay")) {
            long cooldown = player.getMetadata("chat-delay").get(0).asLong() - System.currentTimeMillis();
            if (cooldown > 0) {
                event.setCancelled(true);
                Common.sendMessage(player, CC.RED + "請等待 " + CC.BOLD + Maid.FORMAT.format(cooldown / 1000.0) + CC.RESET + CC.RED + " 秒後再發言");
                return;
            }
        }

        player.setMetadata("chat-delay", new FixedMetadataValue(plugin, System.currentTimeMillis() + (delay * 1000L)));
    }

}

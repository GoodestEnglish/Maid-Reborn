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
import org.jetbrains.annotations.NotNull;
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
                return Common.text((viewer instanceof Player ? "" : "(真實名稱為 " + user.getRealName() + ")") + user.getDisplayName(true) + CC.GRAY + ":" + CC.RESET + " ").append(message.color(TextColor.fromHexString(rank.getChatColor())));
            }
        };
        event.renderer(renderer);
    }

}

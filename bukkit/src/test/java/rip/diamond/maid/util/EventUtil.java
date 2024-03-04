package rip.diamond.maid.util;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventUtil {

    public static String DEFAULT_MESSAGE = "Running maid test environment";

    public static AsyncChatEvent newAsyncChatEvent(Player player, Audience... viewers) {
        Component text = Common.text(DEFAULT_MESSAGE);
        return new AsyncChatEvent(
                true,
                player,
                new HashSet<>(Set.of(viewers)),
                ChatRenderer.defaultRenderer(),
                text,
                text,
                SignedMessage.system(DEFAULT_MESSAGE, text)
        );
    }

    public static AsyncPlayerPreLoginEvent newAsyncPlayerPreLoginEvent(Player player) {
        return new AsyncPlayerPreLoginEvent(player.getName(), player.getAddress().getAddress(), player.getUniqueId());
    }

    public static PlayerLoginEvent newPlayerLoginEvent(Player player) {
        return new PlayerLoginEvent(player, player.getAddress().getHostString(), player.getAddress().getAddress());
    }

    public static PlayerJoinEvent newPlayerJoinEvent(Player player) {
        return new PlayerJoinEvent(player, MiniMessage.miniMessage().deserialize("<name> has joined the Server!", Placeholder.component("name", player.displayName())));
    }

}

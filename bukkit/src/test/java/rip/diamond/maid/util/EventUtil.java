package rip.diamond.maid.util;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

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

}

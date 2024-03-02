package rip.diamond.maid.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.UserSettings;
import rip.diamond.maid.api.user.chat.ChatRoomType;
import rip.diamond.maid.api.user.chat.IChatRoom;
import rip.diamond.maid.player.UserManager;
import rip.diamond.maid.redis.packets.bukkit.chat.StaffMessagePacket;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.MaidPermission;

@RequiredArgsConstructor
public class ChatListener implements Listener {

    private final Plugin plugin;
    private final IMaidAPI api;
    private final ChatManager chatManager;
    private final UserManager userManager;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatFormat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        IUser user = userManager.getUserNow(player.getUniqueId());
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
        if (chatManager.isMuted()) {
            event.setCancelled(true);
            Common.sendMessage(player, CC.RED + "聊天室暫時被關閉了, 你暫時無法再聊天室說話");
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChatDelay(AsyncChatEvent event) {
        Player player = event.getPlayer();
        int delay = chatManager.getDelay();

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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChatMessaging(AsyncChatEvent event) {
        Player player = event.getPlayer();
        IUser user = userManager.getUserNow(player.getUniqueId());
        GlobalUser sender = GlobalUser.of(user, api);
        IChatRoom room = user.getChatRoom();

        if (room.getType() == ChatRoomType.STAFF && player.hasPermission(MaidPermission.SETTINGS_STAFF_CHAT)) {
            event.setCancelled(true);
            api.getPacketHandler().send(new StaffMessagePacket(api.getPlatform().getServerID(), sender, MiniMessage.miniMessage().serialize(event.message())));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChatSettings(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (!userManager.isOn(player.getUniqueId(), UserSettings.GLOBAL_MESSAGE)) {
            event.setCancelled(true);
            Common.sendMessage(player, CC.RED + "你必須要在設定開啟聊天室才能發送訊息");
            return;
        }

        event.viewers().removeIf(audience -> audience instanceof Player target && !userManager.isOn(target.getUniqueId(), UserSettings.GLOBAL_MESSAGE));
    }

}

package rip.diamond.maid.redis.packets.bukkit.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.api.user.UserSettings;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;

@Getter
@RequiredArgsConstructor
public class StaffMessagePacket implements Packet {

    private final String from;
    private final GlobalUser sender;
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Maid.INSTANCE.getUserManager().isOn(player.getUniqueId(), UserSettings.STAFF_CHAT) && player.hasPermission(MaidPermission.SETTINGS_STAFF_CHAT)) {
                Common.sendMessage(player, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "SC" + CC.DARK_GRAY + "] (" + CC.BLUE + from + CC.DARK_GRAY + ") " + CC.RESET + sender.getDisplayName() + CC.WHITE + ": " + CC.GRAY + message);
            }
        }
    }
}

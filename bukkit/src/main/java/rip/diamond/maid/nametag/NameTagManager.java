package rip.diamond.maid.nametag;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.task.NameTagUpdateTask;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.extend.MaidManager;
import rip.diamond.maid.util.packetwrapper.wrappers.play.clientbound.WrapperPlayServerScoreboardTeam;

import java.util.Collections;

public class NameTagManager extends MaidManager {

    public NameTagManager() {
        new NameTagUpdateTask();
    }

    public void sendNameTag(Player player) {
        IUser user = plugin.getUserManager().getUserNow(player.getUniqueId());
        IRank rank = user.getRank();

        String teamName = "maid-" + rank.getName();
        WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
        WrapperPlayServerScoreboardTeam.WrappedParameters parameters = new WrapperPlayServerScoreboardTeam.WrappedParameters(
                WrappedChatComponent.fromText(teamName),
                WrappedChatComponent.fromJson(JSONComponentSerializer.json().serialize(Common.text(rank.getPrefix()))),
                WrappedChatComponent.fromJson(JSONComponentSerializer.json().serialize(Common.text(rank.getSuffix()))),
                "always",
                "always",
                ChatColor.valueOf(NamedTextColor.nearestTo(TextColor.fromHexString(rank.getColor())).toString().toUpperCase()),
                0
        );

        wrapper.setPlayers(Collections.singletonList(user.getName()));
        wrapper.setMethodEnum(WrapperPlayServerScoreboardTeam.Method.CREATE_TEAM);
        wrapper.setName(teamName);
        wrapper.setParameters(parameters);

        wrapper.broadcastPacket();
    }

}

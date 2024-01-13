package rip.diamond.maid.command;

import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.command.annotation.Text;

import java.util.List;

@Require(MaidPermission.MESSAGE)
public class MessageCommand {

    @Command(name = "", desc = "私訊一位玩家")
    public void root(@Sender Player player, String targetName, @Text String message) {
        IUser user = Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();
        List<IPunishment> punishments = user.getActivePunishments(ImmutableList.of(IPunishment.PunishmentType.MUTE));

        if (!punishments.isEmpty()) {
            Common.sendMessage(player, Maid.INSTANCE.getPunishmentManager().getPunishmentMessage(punishments.get(0)));
            return;
        }

        if (player.getName().equalsIgnoreCase(targetName)) {
            Common.sendMessage(player, CC.RED + "你無法傳送訊息給自己");
            return;
        }

        GlobalUser target = (GlobalUser) Maid.INSTANCE.getServerManager().getGlobalUsers().values().stream().filter(globalUser -> globalUser.getName().equalsIgnoreCase(targetName)).findAny().orElse(null);
        if (target == null) {
            Common.sendMessage(player, CC.RED + "玩家 '" + targetName + "' 不在線上");
            return;
        }

        Maid.INSTANCE.getChatManager().sendDirectMessage(user, target, message);
    }

}

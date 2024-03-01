package rip.diamond.maid.command;

import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.util.MaidPermission;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.UserSettings;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.command.annotation.Text;
import rip.diamond.maid.util.extend.MaidCommand;

import java.util.List;

@Require(MaidPermission.MESSAGE)
public class MessageCommand extends MaidCommand {

    @Command(name = "", desc = "私訊一位玩家")
    public void root(@Sender Player sender, String targetName, @Text String message) {
        IUser user = plugin.getUserManager().getUserNow(sender.getUniqueId());
        List<IPunishment> punishments = user.getActivePunishments(ImmutableList.of(IPunishment.PunishmentType.MUTE));

        if (!punishments.isEmpty()) {
            Common.sendMessage(sender, Maid.INSTANCE.getPunishmentManager().getPunishmentMessage(punishments.get(0)));
            return;
        }

        if (!Maid.INSTANCE.getUserManager().isOn(user, UserSettings.PRIVATE_MESSAGE)) {
            Common.sendMessage(sender, CC.RED + "你必須要在設定開啟私人信息才能發送私人信息");
            return;
        }

        if (sender.getName().equalsIgnoreCase(targetName)) {
            Common.sendMessage(sender, CC.RED + "你無法傳送訊息給自己");
            return;
        }

        GlobalUser target = (GlobalUser) Maid.INSTANCE.getServerManager().getGlobalUsers().values().stream().filter(globalUser -> globalUser.getName().equalsIgnoreCase(targetName)).findAny().orElse(null);
        if (target == null) {
            Common.sendMessage(sender, CC.RED + "玩家 '" + targetName + "' 不在線上");
            return;
        }
        if (!Maid.INSTANCE.getUserManager().isOn(target, UserSettings.PRIVATE_MESSAGE) && !sender.hasPermission(MaidPermission.BYPASS_PRIVATE_MESSAGE)) {
            Common.sendMessage(sender, CC.RED + target.getSimpleDisplayName() + CC.RED + " 已關閉私人訊息功能, 無法傳送私人訊息給對方");
            return;
        }

        Maid.INSTANCE.getChatManager().sendDirectMessage(user, target, message);
    }

}

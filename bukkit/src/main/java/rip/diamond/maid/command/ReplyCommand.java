package rip.diamond.maid.command;

import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.api.server.IGlobalUser;
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

import java.util.List;
import java.util.UUID;

@Require(MaidPermission.REPLY)
public class ReplyCommand {

    @Command(name = "", desc = "回覆一位玩家")
    public void root(@Sender Player player, @Text String message) {
        IUser user = Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();
        List<IPunishment> punishments = user.getActivePunishments(ImmutableList.of(IPunishment.PunishmentType.MUTE));

        if (!punishments.isEmpty()) {
            Common.sendMessage(player, Maid.INSTANCE.getPunishmentManager().getPunishmentMessage(punishments.get(0)));
            return;
        }

        if (!Maid.INSTANCE.getUserManager().isOn(user, UserSettings.PRIVATE_MESSAGE)) {
            Common.sendMessage(player, CC.RED + "你必須要在設定開啟私人信息才能發送私人信息");
            return;
        }

        UUID targetUUID = user.getChatRoom().getMessageTo();
        if (targetUUID == null) {
            Common.sendMessage(player, CC.RED + "你沒有可回覆私訊的對象");
            return;
        }

        IGlobalUser target = Maid.INSTANCE.getServerManager().getGlobalUsers().get(targetUUID);
        if (target == null) {
            Common.sendMessage(player, CC.RED + "該玩家已不在線上");
            user.getChatRoom().setMessageTo(null);
            Maid.INSTANCE.getUserManager().saveUser(user);
            return;
        }

        if (!Maid.INSTANCE.getUserManager().isOn(target, UserSettings.PRIVATE_MESSAGE) && !player.hasPermission(MaidPermission.BYPASS_PRIVATE_MESSAGE)) {
            Common.sendMessage(player, CC.RED + target.getSimpleDisplayName() + CC.RED + " 已關閉私人訊息功能, 無法傳送私人訊息給對方");
            return;
        }

        Maid.INSTANCE.getChatManager().sendDirectMessage(user, target, message);
    }

}

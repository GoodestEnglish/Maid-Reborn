package rip.diamond.maid.command;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import rip.diamond.maid.util.MaidPermission;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.UUIDCache;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.command.annotation.Text;
import rip.diamond.maid.util.extend.MaidCommand;

import java.util.UUID;

@Require(MaidPermission.UNBAN)
public class UnbanCommand extends MaidCommand {

    @Command(name = "", desc = "解除封鎖一位玩家")
    public void root(@Sender CommandSender sender, String targetName, @Text String reason) {
        // TODO: 1/3/2024
        UUID targetUUID = UUIDCache.getUUID(targetName).join();

        if (!plugin.getUserManager().hasUser(targetUUID).join()) {
            Common.sendMessage(sender, CC.RED + "未能找到玩家 '" + targetName + "' 的資料");
            return;
        }

        User targetUser = (User) plugin.getUserManager().getUser(targetUUID).join();
        if (targetUser.getActivePunishments(ImmutableList.of(IPunishment.PunishmentType.BAN, IPunishment.PunishmentType.IP_BAN)).isEmpty()) {
            Common.sendMessage(sender, CC.RED + "該玩家沒有任何活躍的封鎖懲罰紀錄");
            return;
        }

        plugin.getPunishmentManager().unpunish(sender, targetUUID, IPunishment.PunishmentType.BAN, reason);
    }

}

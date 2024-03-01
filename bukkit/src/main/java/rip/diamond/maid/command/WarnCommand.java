package rip.diamond.maid.command;

import org.bukkit.command.CommandSender;
import rip.diamond.maid.util.MaidPermission;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.UUIDCache;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.command.annotation.Text;
import rip.diamond.maid.util.extend.MaidCommand;

import java.util.UUID;

@Require(MaidPermission.WARN)
public class WarnCommand extends MaidCommand {

    @Command(name = "", desc = "踢除一位玩家")
    public void root(@Sender CommandSender sender, String targetName, @Text String reason) {
        // TODO: 1/3/2024
        UUID targetUUID = UUIDCache.getUUID(targetName).join();

        if (!plugin.getUserManager().hasUser(targetUUID).join()) {
            Common.sendMessage(sender, CC.RED + "未能找到玩家 '" + targetName + "' 的資料");
            return;
        }

        plugin.getPunishmentManager().punish(sender, targetUUID, IPunishment.PunishmentType.WARN, String.valueOf(TimeUtil.PERMANENT), reason);
    }

}

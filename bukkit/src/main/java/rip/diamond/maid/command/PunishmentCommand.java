package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.player.User;
import rip.diamond.maid.punishment.menu.PunishmentsMenu;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.UUIDCache;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.extend.MaidCommand;

import java.util.UUID;

@Require(MaidPermission.PUNISHMENT)
public class PunishmentCommand extends MaidCommand {

    @Command(name = "", desc = "查看玩家懲罰紀錄")
    public void root(@Sender Player sender, String targetName) {
        UUID uuid = UUIDCache.getUUID(targetName).join();
        if (!plugin.getUserManager().hasUser(uuid).join()) {
            Common.sendMessage(sender, CC.RED + "未能找到玩家 '" + targetName + "' 的資料");
            return;
        }

        User targetUser = (User) plugin.getUserManager().getUser(uuid).join();
        new PunishmentsMenu(sender, targetUser).updateMenu();
    }

}

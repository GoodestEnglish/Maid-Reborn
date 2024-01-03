package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.grant.menu.GrantsMenu;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.UUIDCache;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

import java.util.UUID;

@Require(MaidPermission.GRANTS)
public class GrantsCommand {

    @Command(name = "", desc = "查看/移除職階升級紀錄")
    public void root(@Sender Player sender, String targetName) {
        UUID uuid = UUIDCache.getUUID(targetName).join();
        if (!Maid.INSTANCE.getUserManager().hasUser(uuid).join()) {
            Common.sendMessage(sender, CC.RED + "未能找到玩家 '" + targetName + "' 的資料");
            return;
        }

        User targetUser = (User) Maid.INSTANCE.getUserManager().getUser(uuid).join();
        new GrantsMenu(sender, targetUser).updateMenu();
    }

}

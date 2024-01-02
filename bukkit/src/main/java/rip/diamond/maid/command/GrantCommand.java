package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.grant.menu.GrantsMenu;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.UUIDCache;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Sender;

public class GrantCommand {

    @Command(name = "", desc = "查看/移除職階升級紀錄")
    public void root(@Sender Player sender, String targetName) {
        User targetUser = (User) Maid.INSTANCE.getUserManager().getUser(UUIDCache.getUUID(targetName));

        new GrantsMenu(sender, targetUser).updateMenu();
    }

}

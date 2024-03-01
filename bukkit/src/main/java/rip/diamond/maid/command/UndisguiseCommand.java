package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.util.MaidPermission;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.extend.MaidCommand;

@Require(MaidPermission.UNDISGUISE)
public class UndisguiseCommand extends MaidCommand {

    @Command(name = "", desc = "解除現時的偽裝")
    public void root(@Sender Player sender) {
        IUser user = plugin.getUserManager().getUserNow(sender.getUniqueId());
        if (user.getDisguise() == null) {
            Common.sendMessage(sender, CC.RED + "錯誤: 你並沒有設置任何的偽裝");
            return;
        }
        plugin.getDisguiseManager().unDisguise(sender);
    }

}

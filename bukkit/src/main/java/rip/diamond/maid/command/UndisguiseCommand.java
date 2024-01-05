package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

@Require(MaidPermission.UNDISGUISE)
public class UndisguiseCommand {

    @Command(name = "", desc = "解除現時的偽裝")
    public void root(@Sender Player sender) {
        IUser user = Maid.INSTANCE.getUserManager().getUser(sender.getUniqueId()).join();
        if (user.getDisguise() == null) {
            Common.sendMessage(sender, CC.RED + "錯誤: 你並沒有設置任何的偽裝");
            return;
        }
        Maid.INSTANCE.getDisguiseManager().unDisguise(sender);
    }

}

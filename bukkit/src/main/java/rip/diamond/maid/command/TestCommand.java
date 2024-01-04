package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Sender;

public class TestCommand {

    @Command(name = "", desc = "測試")
    public void root(@Sender Player player) {
        if (player.getName().equals("GoodestEnglish")) {
            User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();
            Common.sendMessage(player, player.hasPermission(MaidPermission.GRANT) + "");
            Common.sendMessage(player, user.containPermission(MaidPermission.GRANT, true) + "");
            Common.sendMessage(player, "<green>done");
        }
    }
}

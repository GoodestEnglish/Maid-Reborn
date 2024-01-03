package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.grant.Grant;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

@Require("*")
public class TestCommand {

    @Command(name = "", desc = "測試")
    public void root(@Sender Player player) {
        User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();
        user.addGrant(new Grant(user, Maid.INSTANCE.getRankManager().getRanksInOrder().get(0), user, "Custom", System.currentTimeMillis(), TimeUtil.PERMANENT));
        Common.sendMessage(player, "<green>done");
    }

}

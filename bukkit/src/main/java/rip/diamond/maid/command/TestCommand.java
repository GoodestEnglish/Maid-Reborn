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

public class TestCommand {

    @Command(name = "", desc = "測試")
    @Require("*")
    public void root(@Sender Player player, int h) {
        User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId());
        for (int i = 0; i < h; i++) {
            user.addGrant(new Grant(user, Maid.INSTANCE.getRankManager().getDefaultRank(), User.CONSOLE, "預設職階", System.currentTimeMillis(), TimeUtil.PERMANENT));
        }
        Common.sendMessage(player, "<green>done");
    }

}

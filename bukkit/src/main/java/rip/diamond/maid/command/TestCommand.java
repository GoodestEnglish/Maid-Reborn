package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.extend.MaidCommand;

@Require("maid.command.test")
public class TestCommand extends MaidCommand {

    @Command(name = "1", desc = "測試")
    public void root(@Sender Player player, String duration_) {
        User user = (User) plugin.getUserManager().getUser(player.getUniqueId()).join();

        long duration = TimeUtil.getDuration(duration_);
        Common.sendMessage(player, duration + "");

        Common.sendMessage(player, "<green>done");
    }
}

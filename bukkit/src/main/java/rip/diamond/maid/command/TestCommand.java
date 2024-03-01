package rip.diamond.maid.command;

import org.bukkit.ChatColor;
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
    public void root(@Sender Player player, String text) {
        String s = ChatColor.translateAlternateColorCodes('&', text);
        player.sendMessage(s);

        Common.sendMessage(player, "<green>done");
    }
}

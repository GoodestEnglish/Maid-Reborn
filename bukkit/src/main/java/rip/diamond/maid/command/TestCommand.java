package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

public class TestCommand {

    @Command(name = "", desc = "測試")
    @Require("maid.command.test")
    public void root(@Sender Player player) {
        Common.sendMessage(player, "<green>done");
    }

}

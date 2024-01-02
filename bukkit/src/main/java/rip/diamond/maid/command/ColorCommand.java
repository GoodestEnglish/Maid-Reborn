package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

public class ColorCommand {

    @Command(name = "", desc = "查看可用的顏色")
    @Require("maid.command.color")
    public void root(@Sender Player sender) {
        for (CC cc : CC.values()) {
            Common.sendMessage(sender, cc + "我的顏色是 - " + cc.name() + " - " + cc.getColor());
        }
    }

}

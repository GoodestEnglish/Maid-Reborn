package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.rank.menu.RanksMenu;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

public class RankCommand {

    @Command(name = "", desc = "查看所有職階")
    @Require(MaidPermission.RANK)
    public void root(@Sender Player sender) {
        new RanksMenu(sender).updateMenu();
    }

}

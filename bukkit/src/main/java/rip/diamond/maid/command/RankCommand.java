package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.util.MaidPermission;
import rip.diamond.maid.rank.menu.RanksMenu;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.extend.MaidCommand;

@Require(MaidPermission.RANK)
public class RankCommand extends MaidCommand {

    @Command(name = "", desc = "查看所有職階")
    public void root(@Sender Player sender) {
        new RanksMenu(sender).updateMenu();
    }

}

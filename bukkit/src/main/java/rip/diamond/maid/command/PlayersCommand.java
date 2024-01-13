package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.server.menu.PlayersMenu;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

@Require(MaidPermission.PLAYERS)
public class PlayersCommand {

    @Command(name = "", desc = "查看玩家一覽")
    public void root(@Sender Player player) {
        new PlayersMenu(player).updateMenu();
    }

}

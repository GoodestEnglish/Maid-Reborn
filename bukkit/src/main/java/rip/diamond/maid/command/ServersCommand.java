package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.server.menu.ServersMenu;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

@Require(MaidPermission.SERVERS)
public class ServersCommand {

    @Command(name = "", desc = "查看在線伺服器")
    public void root(@Sender Player player) {
        new ServersMenu(player).updateMenu();
    }

}

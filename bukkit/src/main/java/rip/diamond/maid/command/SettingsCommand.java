package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.player.menu.settings.UserSettingsMenu;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

@Require(MaidPermission.SETTINGS)
public class SettingsCommand {

    @Command(name = "", desc = "查看設定")
    public void root(@Sender Player sender) {
        new UserSettingsMenu(sender).updateMenu();
    }

}

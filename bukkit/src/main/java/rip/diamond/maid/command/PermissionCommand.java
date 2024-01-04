package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.permission.PermissionManager;
import rip.diamond.maid.permission.UserPermissible;
import rip.diamond.maid.permission.menu.PermissionInspectMenu;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

public class PermissionCommand {

    @Command(name = "menu", desc = "查看現時 UserPermissible 擁有的權限")
    @Require(MaidPermission.PERMISSION_GUI)
    public void root(@Sender Player player) {
        Common.sendMessage(player, CC.GREEN + "正在開啟...");
        try {
            UserPermissible permissible = (UserPermissible) PermissionManager.HUMAN_ENTITY_PERMISSIBLE_FIELD.get(player);
            new PermissionInspectMenu(player, permissible).updateMenu();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

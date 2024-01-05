package rip.diamond.maid.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.permission.UserPermissible;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Sender;

import static rip.diamond.maid.permission.PermissionManager.HUMAN_ENTITY_PERMISSIBLE_FIELD;

public class TestCommand {

    @Command(name = "1", desc = "測試")
    public void root(@Sender Player player) {
        if (player.getName().equals("GoodestEnglish")) {
            User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();



            Common.sendMessage(player, "<green>done");
        }
    }

    @Command(name = "2", desc = "測試")
    public void root2(@Sender Player player) throws IllegalAccessException {
        if (player.getName().equals("GoodestEnglish")) {
            User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();

            ((UserPermissible) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(player)).recalculatePermissions();

            Common.sendMessage(player, "<green>done");
        }
    }

    @Command(name = "3", desc = "測試")
    public void root3(@Sender Player player, String permission, boolean enabled) throws IllegalAccessException {
        if (player.getName().equals("GoodestEnglish")) {
            User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();

            if (enabled) {
                Bukkit.getServer().getPluginManager().subscribeToPermission(permission, player);
            } else {
                Bukkit.getServer().getPluginManager().unsubscribeFromPermission(permission, player);
            }


            Common.sendMessage(player, "<green>done");
        }
    }
}

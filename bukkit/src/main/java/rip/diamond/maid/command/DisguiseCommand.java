package rip.diamond.maid.command;

import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.disguise.Disguise;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.extend.MaidCommand;

import java.util.Map;

@Require(MaidPermission.DISGUISE)
public class DisguiseCommand extends MaidCommand {

    @Command(name = "", desc = "解除一個偽裝")
    public void root(@Sender Player sender, String username) {
        IUser user = plugin.getUserManager().getUser(sender.getUniqueId()).join();
        if (user.getDisguise() != null) {
            Common.sendMessage(sender, CC.RED + "錯誤: 請先解除當前的偽裝");
            return;
        }
        if (Bukkit.getPlayer(username) != null) {
            Common.sendMessage(sender, CC.RED + "錯誤: 無法偽裝成為在線玩家");
            return;
        }

        Map.Entry<String, ProfileProperty> randomProperty = plugin.getDisguiseManager().getRandomSkin();

        Disguise disguise = new Disguise(username, randomProperty.getKey(), plugin.getRankManager().getDefaultRank().getUniqueID());
        user.setDisguise(disguise);
        plugin.getDisguiseManager().disguise(sender, disguise, false);
    }

}

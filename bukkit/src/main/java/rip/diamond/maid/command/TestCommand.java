package rip.diamond.maid.command;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.disguise.Disguise;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.CraftBukkitImplementation;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Require("maid.command.test")
public class TestCommand {

    @Command(name = "1", desc = "測試")
    public void root(@Sender Player player) {
        User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();


        Common.sendMessage(player, "<green>done");
    }

    @Command(name = "2", desc = "測試")
    public void root2(@Sender Player player) {
        User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();
        Common.log("Using version " + CraftBukkitImplementation.SERVER_PACKAGE_VERSION);
        Maid.INSTANCE.getDisguiseManager().disguise(player, user.getDisguise(), false);
    }

    @Command(name = "3", desc = "測試")
    public void root3(@Sender Player player, Player tProfile) {
        User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();

        ProfileProperty property = tProfile.getPlayerProfile().getProperties().stream().filter(profileProperty -> profileProperty.getName().equals("textures")).findAny().orElse(null);

        PlayerProfile profile = player.getPlayerProfile();
        profile.setProperty(new ProfileProperty("textures", property.getValue(), property.getSignature()));
        player.setPlayerProfile(profile);
    }

    @Command(name = "4", desc = "測試")
    public void root4(@Sender Player player) {
        User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) {
                continue;
            }
            if (other.canSee(player)) {
                other.hidePlayer(Maid.INSTANCE, player);
                other.showPlayer(Maid.INSTANCE, player);
            }
        }
    }
}

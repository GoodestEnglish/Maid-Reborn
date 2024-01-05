package rip.diamond.maid.player;

import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IDisguise;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.grant.Grant;
import rip.diamond.maid.util.*;
import rip.diamond.maid.util.extend.MaidListener;

import java.util.UUID;

public class UserListener extends MaidListener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLoginDoubleLogin(AsyncPlayerPreLoginEvent event) {
        UUID uniqueID = event.getUniqueId();
        Player player = Bukkit.getPlayer(uniqueID);
        if (player != null && player.isConnected()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Common.text(CC.RED + "請再重新登陸前等待三秒..."));
            Tasks.run(() -> player.kick(Common.text(CC.RED + "重複的登入!")));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLoginCreateUser(AsyncPlayerPreLoginEvent event) {
        UUID uniqueID = event.getUniqueId();
        IUser user = plugin.getUserManager().getUser(uniqueID).join();

        user.setRealName(event.getName());
        user.updateSeen();
        user.updateLastServer();
        user.setIP(event.getAddress().getHostAddress());

        plugin.getUserManager().saveUser(user);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginStartupCheck(PlayerLoginEvent event) {
        if (!plugin.getServerManager().isAllowJoin()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Common.text(CC.RED + "伺服器暫時不允許玩家進入, 請稍後再試"));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLoginInjectPermission(PlayerLoginEvent event) {
        //Deny injection when player isn't allow to login
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        //At this point, user data should be present and loaded
        Player player = event.getPlayer();
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();

        try {
            Maid.INSTANCE.getPermissionManager().initPlayer(player);
        } catch (IllegalAccessException e) {
            Common.log("Error: Failed to inject UserPermissible to player '" + user.getRealName() + "'");
            throw new RuntimeException(e);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoinCacheUUID(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUIDCache.insert(player.getUniqueId(), player.getName());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoinCacheTexture(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();

        ProfileProperty property = player.getPlayerProfile().getProperties().stream().filter(profileProperty -> profileProperty.getName().equals("textures")).findAny().orElse(null);
        if (property == null) {
            Common.log("Warning: Cannot find textures property for player '" + user.getRealName() + "'");
            return;
        }
        user.setTexture(property.getValue());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoinDisguise(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();
        IDisguise disguise = user.getDisguise();
        if (disguise != null) {
            Maid.INSTANCE.getDisguiseManager().disguise(player, disguise, true);
        }
    }
}

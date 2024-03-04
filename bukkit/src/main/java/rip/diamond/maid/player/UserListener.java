package rip.diamond.maid.player;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IDisguise;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.UserSettings;
import rip.diamond.maid.mongo.MongoManager;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.RankManager;
import rip.diamond.maid.server.ServerManager;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.UUIDCache;
import rip.diamond.maid.util.task.ITaskRunner;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
public class UserListener implements Listener {

    private final ITaskRunner task;
    private final MongoManager mongoManager;
    private final UserManager userManager;
    private final RankManager rankManager;
    private final ServerManager serverManager;
    private final PunishmentManager punishmentManager;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLoginDoubleLogin(AsyncPlayerPreLoginEvent event) {
        UUID uniqueID = event.getUniqueId();
        Player player = Bukkit.getPlayer(uniqueID);
        if (player != null && player.isConnected()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Common.text(CC.RED + "請再重新登陸前等待三秒..."));
            task.run(() -> player.kick(Common.text(CC.RED + "重複的登入!")));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLoginCreateUser(AsyncPlayerPreLoginEvent event) {
        UUID uniqueID = event.getUniqueId();
        IUser user = userManager.getUser(uniqueID, rankManager, punishmentManager).join(); //join in here is allowed since this event is running async

        user.setRealName(event.getName());
        user.updateSeen();
        user.updateLastServer();
        user.setIP(event.getAddress().getHostAddress());
        Arrays.stream(UserSettings.values()).forEach(settings -> user.getSettings().putIfAbsent(settings, settings.getDefaultOption()));

        for (Document document : mongoManager.getUsers().find(Filters.eq("ip", user.getIP()))) {
            UUID uuid = UUID.fromString(document.getString("_id"));
            //Only add alts if the uuid is not the same
            if (!uniqueID.equals(uuid)) {
                user.getAlts().add(uuid);
            }
        }

        userManager.saveUser(user);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginStartupCheck(PlayerLoginEvent event) {
        if (!serverManager.isAllowJoin()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Common.text(CC.RED + "伺服器暫時不允許玩家進入, 請稍後再試"));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLoginInjectPermission(PlayerLoginEvent event) {
        //At this point, user data should be present and loaded
        Player player = event.getPlayer();
        IUser user = userManager.getUserNow(player.getUniqueId());

        try {
            Maid.INSTANCE.getPermissionManager().initPlayer(player);
        } catch (IllegalAccessException e) {
            Common.log("Error: Failed to inject UserPermissible to player '" + user.getRealName() + "'");
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Common.text(CC.RED + "錯誤: 無法注入 UserPermissible"));
            throw new RuntimeException(e);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoinCache(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        IUser user = userManager.getUserNow(player.getUniqueId());

        //Cache UUID and username
        UUIDCache.insert(player.getUniqueId(), player.getName());

        //Cache skin
        ProfileProperty property = player.getPlayerProfile().getProperties().stream().filter(profileProperty -> profileProperty.getName().equals("textures")).findAny().orElse(null);
        if (property == null) {
            Common.log("Warning: Cannot find textures property for player '" + user.getRealName() + "'");
            return;
        }
        user.setTexture(property.getValue());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoinDisguise(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        IUser user = userManager.getUserNow(player.getUniqueId());
        IDisguise disguise = user.getDisguise();
        if (disguise != null) {
            Maid.INSTANCE.getDisguiseManager().disguise(player, disguise, true);
        }
    }
}

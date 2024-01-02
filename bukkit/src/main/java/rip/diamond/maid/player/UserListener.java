package rip.diamond.maid.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.Tasks;
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
        IUser user = plugin.getUserManager().getUser(uniqueID);

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
}

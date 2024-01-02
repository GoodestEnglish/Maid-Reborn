package rip.diamond.maid.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.extend.MaidListener;

public class PlayerListener extends MaidListener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!plugin.getServerManager().isAllowJoin()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Common.text(CC.RED + "伺服器暫時不允許玩家進入, 請稍後再試"));
        }
    }

}

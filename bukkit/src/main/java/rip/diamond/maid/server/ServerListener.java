package rip.diamond.maid.server;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.task.FinishStartupTask;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.extend.MaidListener;

@RequiredArgsConstructor
public class ServerListener implements Listener {

    private final IMaidAPI api;

    @EventHandler
    public void onStartup(ServerLoadEvent event) {
        if (event.getType() == ServerLoadEvent.LoadType.RELOAD) {
            Common.log("偵測到伺服器已重新加載, Maid插件不允許重新加載, 正在重新啟動伺服器");
            Bukkit.shutdown();
        } else if (event.getType() == ServerLoadEvent.LoadType.STARTUP) {
            //運行FinishStartupTask, 讓玩家可以在伺服器開啟五秒後進入
            new FinishStartupTask(api);
        }
    }

}

package rip.diamond.maid.task;

import rip.diamond.maid.api.server.IServer;
import rip.diamond.maid.util.TaskTicker;

public class ServerUpdateTask extends TaskTicker {
    public ServerUpdateTask() {
        super(0, 20, true);
    }

    @Override
    public void onRun() {
        //Remove every server and user which isn't alive for 5 seconds
        plugin.getServerManager().getGlobalUsers().values().removeIf(server -> System.currentTimeMillis() - server.getLastTick() >= 5000L);
        plugin.getServerManager().getServers().values().removeIf(server -> System.currentTimeMillis() - server.getLastTick() >= 5000L);

        //Update the current server
        IServer server = plugin.getServerManager().getCurrentServer();
        server.update();
    }
}

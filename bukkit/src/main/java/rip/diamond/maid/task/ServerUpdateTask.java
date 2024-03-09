package rip.diamond.maid.task;

import rip.diamond.maid.Maid;
import rip.diamond.maid.api.server.IServer;
import rip.diamond.maid.util.TaskTicker;

public class ServerUpdateTask extends TaskTicker {
    public ServerUpdateTask() {
        super(0, 20, true);
    }

    @Override
    public void onRun() {
        //Remove every server and user which isn't alive for 5 seconds
        Maid.INSTANCE.getServerManager().getGlobalUsers().values().removeIf(server -> System.currentTimeMillis() - server.getLastTick() >= 5000L);
        Maid.INSTANCE.getServerManager().getServers().values().removeIf(server -> System.currentTimeMillis() - server.getLastTick() >= 5000L);

        //Update the current server
        IServer server = Maid.INSTANCE.getServerManager().getCurrentServer();
        server.update();
    }
}

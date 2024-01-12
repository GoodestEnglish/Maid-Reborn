package rip.diamond.maid.server;

import lombok.Getter;
import lombok.Setter;
import rip.diamond.maid.api.server.IServer;
import rip.diamond.maid.config.Config;
import rip.diamond.maid.task.ServerUpdateTask;
import rip.diamond.maid.util.extend.MaidManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ServerManager extends MaidManager {

    private final Map<String, IServer> servers = new ConcurrentHashMap<>();
    private final IServer currentServer;
    @Setter private boolean loaded = false;

    public ServerManager() {
        this.currentServer = new Server(Config.SERVER_ID.toString(), System.currentTimeMillis());

        new ServerUpdateTask();
    }

    /**
     * Check if the server allows player to join
     *
     * @return true if the server allows player to join
     */
    public boolean isAllowJoin() {
        return loaded;
    }
}

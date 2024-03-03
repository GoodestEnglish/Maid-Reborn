package rip.diamond.maid.server;

import lombok.Getter;
import lombok.Setter;
import rip.diamond.maid.api.server.IGlobalUser;
import rip.diamond.maid.api.server.IServer;
import rip.diamond.maid.config.ServerConfig;
import rip.diamond.maid.task.ServerUpdateTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ServerManager {

    private final Map<UUID, IGlobalUser> globalUsers = new ConcurrentHashMap<>();
    private final Map<String, IServer> servers = new ConcurrentHashMap<>();
    private final IServer currentServer;
    @Setter private boolean loaded = false;

    public ServerManager(ServerConfig config) {
        this.currentServer = new Server(config.getServerID(), System.currentTimeMillis());

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

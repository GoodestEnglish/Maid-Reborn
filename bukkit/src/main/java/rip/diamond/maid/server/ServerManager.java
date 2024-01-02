package rip.diamond.maid.server;

import lombok.Setter;
import rip.diamond.maid.util.extend.MaidManager;

public class ServerManager extends MaidManager {

    @Setter private boolean loaded = false;

    /**
     * Check if the server allows player to join
     *
     * @return true if the server allows player to join
     */
    public boolean isAllowJoin() {
        return loaded;
    }
}

package rip.diamond.maid.api.server;

import java.util.List;
import java.util.UUID;

public interface IServer {

    /**
     * Get the ID of the server.
     *
     * @return The ID
     */
    String getID();

    /**
     * Get the startup time of the server.
     *
     * @return The startup time
     */
    long getStartupTime();

    /**
     * Get the uptime of the server.
     *
     * @return The uptime
     */
    long getUpTime();

    /**
     * Get the TPS of the server.
     *
     * @return The TPS
     */
    double[] getTps();

    /**
     * Get if the server is loaded or not.
     *
     * @return True if the server is loaded
     */
    boolean isLoaded();

    /**
     * Get if the server is whitelisted or not.
     *
     * @return True if the server is whitelisted
     */
    boolean isWhiteListed();

    /**
     * Get all the online players, as UUID.
     *
     * @return Online players
     */
    List<UUID> getOnlinePlayers();

    /**
     * Get the max players amount.
     *
     * @return The max players amount
     */
    int getMaxPlayers();

    /**
     * Get if the chat is muted or not.
     *
     * @return True if the chat is muted
     */
    boolean isChatMuted();

    /**
     * Get the current chat delay.
     *
     * @return The chat delay
     */
    long getChatDelay();

    /**
     * Get the last tick received by this server.
     *
     * @return The last tick
     */
    long getLastTick();

    /**
     * Update the server data
     */
    void update();

}

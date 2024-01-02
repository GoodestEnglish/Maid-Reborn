package rip.diamond.maid.api.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IUser {

    /**
     * Get the player's UUID.
     *
     * @return The player's UUID
     */
    @SerializedName("_id")
    UUID getUniqueId();

    /**
     * Get the player's name.
     * If the player is disguised, this will return player's disguise name.
     *
     * @return The player's name
     */
    String getName();

    /**
     * Get the player's real name.
     * This method will ignore the current disguise on the player, which means it will return the real/original name.
     *
     * @return The player's real name
     */
    String getRealName();

    /**
     * Set the player's real name.
     * This method usually used to sync the player's name to their user profile.
     *
     * @param name The name to be set
     */
    void setRealName(String name);

    /**
     * Get the player's first seen.
     *
     * @return The player's first seen
     */
    long getFirstSeen();

    /**
     * Get the player's last seen.
     *
     * @return The player's last seen
     */
    long getLastSeen();

    /**
     * Update the player's first/last seen.
     */
    void updateSeen();

    /**
     * Get the player's last server.
     *
     * @return The player's last server
     */
    String getLastServer();

    /**
     * Update the player's last server.
     */
    void updateLastServer();

    /**
     * Get the player's current/last seen IP Address.
     *
     * @return The last seen IP Address
     */
    String getIP();

    /**
     * Set the player's current/last seen IP Address.
     *
     * @param ip The current player IP Address
     */
    void setIP(String ip);

    /**
     * Get the player's IP Address history.
     *
     * @return The player's all IP Address history
     */
    Set<String> getIPHistory();

    /**
     * Get the player's all permissions.
     * Note: This will not include permissions for player's rank.
     *
     * @return The player's all permissions
     */
    Set<String> getPermissions();

    /**
     * Get the player's all grants.
     *
     * @return The player's all grants
     */
    List<? extends IGrant> getGrants();

    /**
     * Add a new grant to the user.
     *
     * @param grant The grant
     */
    void addGrant(IGrant grant);
}

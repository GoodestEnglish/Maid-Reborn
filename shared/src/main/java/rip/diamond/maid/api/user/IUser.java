package rip.diamond.maid.api.user;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public interface IUser {

    /**
     * Get the player's UUID
     *
     * @return The player's UUID
     */
    @SerializedName("_id")
    UUID getUniqueId();

    /**
     * Get the player's name
     * If the player is disguised, this will return player's disguise name
     *
     * @return The player's name
     */
    String getName();

    /**
     * Get the player's real name
     * This method will ignore the current disguise on the player, which means it will return the real/original name
     *
     * @return The player's real name
     */
    String getRealName();

    /**
     * Get the player's first login
     *
     * @return The player's first login
     */
    long getFirstLogin();

    /**
     * Get the player's last seen
     *
     * @return The player's last seen
     */
    long getLastSeen();

    /**
     * Set the player's last seen
     * If the player cannot join the proxy successfully, their last seen will not be logged
     */
    void setLastSeen();
}

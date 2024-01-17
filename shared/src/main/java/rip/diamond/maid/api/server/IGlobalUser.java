package rip.diamond.maid.api.server;

import java.util.UUID;

public interface IGlobalUser {

    /**
     * Get the player's UUID.
     *
     * @return The player's UUID
     */
    UUID getUniqueID();

    /**
     * Get the player's name.
     *
     * @return The player's name
     */
    String getName();

    /**
     * Get the player's display name, as simple form.
     * The display name doesn't contain rank prefix and suffix, but contain the color of the rank.
     *
     * @return The display name
     */
    String getSimpleDisplayName();

    /**
     * Get the name, with color, prefix and suffix.
     *
     * @return The display name
     */
    String getDisplayName();

    /**
     * Get the player's texture.
     *
     * @return The texture
     */
    String getTexture();

    /**
     * Get the player current server.
     *
     * @return The server ID
     */
    String getCurrentServer();

    /**
     * Get the last tick received by the server the player is in.
     *
     * @return The last tick
     */
    long getLastTick();

}

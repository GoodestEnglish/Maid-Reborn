package rip.diamond.maid.api.user;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public interface IRank {

    /**
     * Get the rank's unique ID.
     *
     * @return The rank's unique ID
     */
    @SerializedName("_id")
    UUID getUniqueID();

    /**
     * Get the rank's name.
     *
     * @return The rank's name
     */
    String getName();

    /**
     * Set the rank's name.
     *
     * @param name The new name of the rank
     */
    void setName(String name);

    /**
     * Get the rank's prefix.
     *
     * @return The rank's prefix
     */
    String getPrefix();

    /**
     * Set the rank's prefix.
     *
     * @param prefix The new prefix of the rank
     */
    void setPrefix(String prefix);

    /**
     * Get the rank's suffix.
     *
     * @return The rank's suffix
     */
    String getSuffix();

    /**
     * Set the rank's suffix.
     *
     * @param suffix The new suffix of the rank
     */
    void setSuffix(String suffix);

    /**
     * Get the rank's display name.
     *
     * @return The rank's display name
     */
    String getDisplayName();

    /**
     * Set the rank's display name.
     *
     * @param displayName The new display name of the rank
     */
    void setDisplayName(String displayName);

    /**
     * Check if the rank is a default rank.
     * Note: Only one default rank can be in the database and cache.
     *
     * @return True of the rank is a default rank
     */
    boolean isDefault();

    /**
     * Set the rank default state
     *
     * @param default_ The new default state
     */
    void setDefault(boolean default_);

}

package rip.diamond.maid.api.user;

import com.google.gson.annotations.SerializedName;
import rip.diamond.maid.api.user.permission.Permission;
import rip.diamond.maid.api.user.permission.RankPermission;

import java.util.Set;
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
     * Get the rank's color.
     *
     * @return The rank's color
     */
    String getColor();

    /**
     * Set the rank's color.
     *
     * @param name The new color of the rank
     */
    void setColor(String name);

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
     * Set the rank default state.
     *
     * @param default_ The new default state
     */
    void setDefault(boolean default_);

    /**
     * Get the rank's priority.
     *
     * @return The rank's priority
     */
    int getPriority();

    /**
     * Set the rank's priority.
     *
     * @param priority The new rank's priority
     */
    void setPriority(int priority);

    /**
     * Get the rank's permissions.
     *
     * @return The rank's permissions
     */
    Set<RankPermission> getPermissions();

    /**
     * Get the rank's permissions, including parent rank's permissions.
     *
     * @return The rank's permissions
     */
    Set<RankPermission> getAllPermissions();

    /**
     * Add a new permission.
     *
     * @param permission The permission
     */
    void addPermission(String permission);

    /**
     * Remove an existing permission.
     *
     * @param permission The permission which needs to be removed
     */
    void removePermission(String permission);

    /**
     * Add a parent rank to the rank
     *
     * @param parent The rank unique ID
     */
    void addParent(UUID parent);

    /**
     * Remove a parent rank from the rank
     *
     * @param parent The rank unique ID
     */
    void removeParent(UUID parent);

}

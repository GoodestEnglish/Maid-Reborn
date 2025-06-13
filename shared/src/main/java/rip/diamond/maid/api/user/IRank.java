package rip.diamond.maid.api.user;

import com.google.gson.annotations.SerializedName;
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
     * Get the rank's color, in hex color (Ex: #FF5733)
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
     * Get the rank's chat color.
     *
     * @return The rank's chat color
     */
    String getChatColor();

    /**
     * Set the rank's chat color.
     * If the player is disguised, this will return the disguise's rank chat color.
     *
     * @param hexColor The new chat color of the rank
     */
    void setChatColor(String hexColor);

    /**
     * Get the rank's display name.
     *
     * @return The rank's display name
     */
    String getDisplayName(boolean withColor);

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
     * The permission is ordered by the priority of the permission.
     *
     * @return The rank's permissions
     */
    Set<RankPermission> getAllPermissions();

    /**
     * Check if the rank contain the permission.
     *
     * @param permission The permission
     * @return True if the rank has the permission
     */
    boolean containPermission(String permission);

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
     * Get all parent ranks in this rank, in UUID.
     *
     * @return All parents ranks
     */
    Set<UUID> getParents();

    /**
     * Check if the rank contain the parent.
     *
     * @param parent The parent
     * @return True if the rank has the parent
     */
    boolean containParent(UUID parent);

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

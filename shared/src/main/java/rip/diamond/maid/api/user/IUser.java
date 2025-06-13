package rip.diamond.maid.api.user;

import com.google.gson.annotations.SerializedName;
import rip.diamond.maid.api.user.chat.IChatRoom;
import rip.diamond.maid.api.user.permission.Permission;
import rip.diamond.maid.api.user.permission.UserPermission;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IUser {

    /**
     * Get the player's UUID.
     *
     * @return The player's UUID
     */
    @SerializedName("_id")
    UUID getUniqueID();

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
     * Get the player's display name, as simple form
     * The display name doesn't contain rank prefix and suffix, but contain the color of the rank
     *
     * @param disguise Should system return disguised display name instead of real display name
     * @return The display name
     */
    String getSimpleDisplayName(boolean disguise);

    /**
     * Get the player's display name.
     * The display name contains rank prefix and suffix.
     *
     * @param disguise Should system return disguised display name instead of real display name
     * @return The display name
     */
    String getDisplayName(boolean disguise);

    /**
     * Get the player's texture.
     *
     * @return The texture
     */
    String getTexture();

    /**
     * Set the player's texture.
     *
     * @param texture The texture to be set
     */
    void setTexture(String texture);

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
     * Get the player's alts according to their IP.
     * This is a mutable set.
     *
     * @return The player's alts
     */
    Set<UUID> getAlts();

    /**
     * Check if the user contain the permission.
     *
     * @param permission The permission
     * @param includeRank Should the function calculate rank permission also
     * @return True if the user has the permission
     */
    boolean containPermission(String permission, boolean includeRank);

    /**
     * Add a new permission.
     *
     * @param permission The permission
     */
    void addPermission(String permission);

    /**
     * Remove an existing permission.
     *
     * @param permission The permission
     */
    void removePermission(String permission);

    /**
     * Get the player's all permissions.
     * Note: This will not include permissions for player's rank.
     *
     * @return The player's all permissions
     */
    Set<UserPermission> getPermissions();

    /**
     * Get the player's all permissions, including player's rank.
     * The permission is ordered by the priority of the permission.
     *
     * @return The player's all permissions
     */
    Set<Permission> getAllPermissions();

    /**
     * Get the player's all grants.
     *
     * @return The player's all grants
     */
    List<? extends IGrant> getGrants();

    /**
     * Get the player's all active grants.
     *
     * @return The player's all active grants
     */
    List<? extends IGrant> getActiveGrants();

    /**
     * Add a new grant to the user.
     *
     * @param grant The grant
     */
    void addGrant(IGrant grant);

    /**
     * Get all user owned ranks based on grants.
     * The ranks are ordered by the priority of the rank.
     *
     * @return All user owned ranks
     */
    Set<IRank> getRanks();

    /**
     * Get the highest priority rank based on grants.
     * If the player is disguised, it will return the disguised rank.
     *
     * @return The highest priority rank
     */
    IRank getRank();

    /**
     * Get the highest priority rank based on grants.
     *
     * @return The highest priority rank
     */
    IRank getRealRank();

    /**
     * Get the current disguise.
     * If there's no disguise currently. This will return null.
     *
     * @return The current disguise
     */
    IDisguise getDisguise();

    /**
     * Set the current disguise.
     *
     * @param disguise The disguise
     */
    void setDisguise(IDisguise disguise);

    /**
     * Get the user's chat room.
     * Chat room stores which channel should the user send the message to.
     *
     * @return The chat room
     */
    IChatRoom getChatRoom();

    /**
     * Get the player settings, as a Map
     */
    Map<UserSettings, String> getSettings();

    /**
     * Get all the punishments associated with this user.
     *
     * @return List of punishments
     */
    List<IPunishment> getPunishments();

    /**
     * Get all the punishments associated with this user.
     *
     * @param types The types of punishment to filter
     * @return List of punishments
     */
    List<IPunishment> getPunishments(List<IPunishment.PunishmentType> types);

    /**
     * Get all the active punishments associated with this user.
     *
     * @return List of punishments
     */
    List<IPunishment> getActivePunishments();

    /**
     * Get all the active punishments associated with this user.
     *
     * @param types The types of punishment to filter
     * @return List of punishments
     */
    List<IPunishment> getActivePunishments(List<IPunishment.PunishmentType> types);
}

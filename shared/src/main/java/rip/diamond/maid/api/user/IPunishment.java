package rip.diamond.maid.api.user;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public interface IPunishment {

    /**
     * Get the punishment's unique ID.
     *
     * @return The punishment's unique ID
     */
    @SerializedName("_id")
    UUID getUniqueID();

    /**
     * Get the type of this punishment.
     *
     * @return The type of this punishment
     */
    PunishmentType getType();

    /**
     * Get the player's unique ID associated to this punishment.
     *
     * @return The player's unique ID
     */
    UUID getUser();

    /**
     * Get the issuer's unique ID associated to this punishment.
     *
     * @return The issuer's unique ID
     */
    UUID getIssuer();

    /**
     * Get the issuer's name associated to this punishment.
     *
     * @return The issuer's name
     */
    String getIssuerName();

    /**
     * Get the reason why this punishment was created/exists.
     *
     * @return The reason
     */
    String getReason();

    /**
     * Get the issued time of this punishment.
     *
     * @return The time when the punishment was issued
     */
    long getIssuedAt();

    /**
     * Get the duration of this punishment.
     *
     * @return The duration
     */
    long getDuration();

    /**
     * Get the revoker of this punishment.
     *
     * @return The revoker's unique ID
     */
    UUID getRevoker();

    /**
     * Get the revoker of this punishment.
     *
     * @return The revoker's name
     */
    String getRevokerName();

    /**
     * Get the revoke reason of this punishment.
     *
     * @return The reason why this punishment was revoked
     */
    String getRevokedReason();

    /**
     * Get the revoke time of this punishment.
     *
     * @return The time when the punishment is revoked
     */
    long getRevokedAt();

    /**
     * Revoke the punishment.
     *
     * @param revoker The user which cause the revoke
     * @param revokedReason The reason why this punishment was revoked
     */
    void revoke(IUser revoker, String revokedReason);

    /**
     * Check if the punishment is active or not.
     *
     * @return True if the punishment is active
     */
    boolean isActive();

    enum PunishmentType {
        WARN, KICK, MUTE, BAN, IP_BAN;
    }
}

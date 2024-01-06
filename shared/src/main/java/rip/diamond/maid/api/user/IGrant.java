package rip.diamond.maid.api.user;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public interface IGrant {

    /**
     * Get the grant's unique ID.
     *
     * @return The grant's unique ID
     */
    @SerializedName("_id")
    UUID getUniqueID();

    /**
     * Get the grant's associated rank.
     *
     * @return The rank which represent to this grant
     */
    IRank getRank();

    /**
     * Get the player's unique ID associated to this grant.
     *
     * @return The player's unique ID
     */
    UUID getUser();

    /**
     * Get the issuer's unique ID associated to this grant.
     *
     * @return The issuer's unique ID
     */
    UUID getIssuer();

    /**
     * Get the issuer's name associated to this grant.
     *
     * @return The issuer's name
     */
    String getIssuerName();

    /**
     * Get the reason why this grant was created/exists.
     *
     * @return The reason
     */
    String getReason();

    /**
     * Get the issued time of this grant.
     *
     * @return The time when the grant was issued
     */
    long getIssuedAt();

    /**
     * Get the duration of this grant.
     *
     * @return The duration
     */
    long getDuration();

    /**
     * Get the revoker of this grant.
     *
     * @return The revoker's unique ID
     */
    UUID getRevoker();

    /**
     * Get the revoker of this grant.
     *
     * @return The revoker's name
     */
    String getRevokerName();

    /**
     * Get the revoke reason of this grant.
     *
     * @return The reason why this grant was revoked
     */
    String getRevokedReason();

    /**
     * Get the revoke time of this grant.
     *
     * @return The time when the grant is revoked
     */
    long getRevokedAt();

    /**
     * Revoke the grant.
     *
     * @param revoker The user which cause the revoke
     * @param revokedReason The reason why this grant was revoked
     */
    void revoke(IUser revoker, String revokedReason);

    /**
     * Check if the grant is active or not.
     *
     * @return True if the grant is active
     */
    boolean isActive();
}

package rip.diamond.maid.api.user;

import java.util.UUID;

public interface IDisguise {

    /**
     * Get the disguised username
     *
     * @return The disguised username
     */
    String getName();

    /**
     * Get the disguised UUID
     *
     * @return The disguised UUID
     */
    UUID getUniqueID();

    /**
     * Get the skin texture value
     *
     * @return The skin texture value
     */
    String getSkinValue();

    /**
     * Get the skin texture signature
     *
     * @return The skin texture signature
     */
    String getSkinSignature();

    /**
     * Get the disguised rank UUID
     *
     * @return Disguised rank UUID
     */
    UUID getDisguiseRankUUID();

}

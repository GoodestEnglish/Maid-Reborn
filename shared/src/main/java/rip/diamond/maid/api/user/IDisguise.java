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
     * Get the skin texture owner's name
     *
     * @return The skin texture owner's name
     */
    String getSkinName();

    /**
     * Get the disguised rank UUID
     *
     * @return Disguised rank UUID
     */
    UUID getDisguiseRankUUID();

}

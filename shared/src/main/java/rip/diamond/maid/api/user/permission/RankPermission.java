package rip.diamond.maid.api.user.permission;

import lombok.Getter;
import rip.diamond.maid.api.user.IRank;

import java.util.UUID;

public class RankPermission extends Permission {

    @Getter private final UUID associatedRankUUID;

    public RankPermission(String permission, IRank associatedRank) {
        super(permission, System.currentTimeMillis());
        this.associatedRankUUID = associatedRank.getUniqueID();
    }
}

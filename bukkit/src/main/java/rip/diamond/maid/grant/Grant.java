package rip.diamond.maid.grant;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IGrant;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.UUID;

@Getter
@Setter
public class Grant implements IGrant {
    @SerializedName("_id")
    private final UUID uniqueID;
    private final UUID rankId, user, issuer;
    private final String issuerName, reason;
    private final long issuedAt, duration;

    private UUID revoker;
    private String revokerName, revokedReason;
    private long revokedAt = 0;

    public Grant(IUser user, IRank rank, IUser issuer, String reason, long issuedAt, long duration) {
        this.uniqueID = UUID.randomUUID();
        this.rankId = rank.getUniqueID();
        this.user = user.getUniqueID();
        this.issuer = issuer.getUniqueID();
        this.issuerName = issuer.getRealName();
        this.reason = reason;
        this.issuedAt = issuedAt;
        this.duration = duration;
    }

    public static Grant of(Document document) {
        return GsonProvider.GSON.fromJson(document.toJson(), Grant.class);
    }

    @Override
    public IRank getRank() {
        return Maid.INSTANCE.getRankManager().getRanks().get(rankId);
    }

    @Override
    public String getRevokerName() {
        return (issuedAt + duration) > System.currentTimeMillis() ? revokerName : "---";
    }

    @Override
    public String getRevokedReason() {
        return (issuedAt + duration) > System.currentTimeMillis() ? revokedReason : "---";
    }

    @Override
    public void revoke(IUser revoker, String revokedReason) {
        this.revoker = revoker.getUniqueID();
        this.revokerName = revoker.getRealName();
        this.revokedReason = revokedReason;
        this.revokedAt = System.currentTimeMillis();
    }

    @Override
    public boolean isActive() {
        if (revokedAt != 0) {
            return false;
        }
        if (duration == TimeUtil.PERMANENT) {
            return true;
        }
        return (issuedAt + duration) > System.currentTimeMillis();
    }
}

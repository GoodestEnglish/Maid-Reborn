package rip.diamond.maid.grant;

import com.google.gson.annotations.SerializedName;
import org.bson.Document;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IGrant;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.UUID;

public class Grant implements IGrant {
    @SerializedName("_id")
    private final UUID uniqueID;
    private final UUID rankId, user, issuer;
    private final String issuerName, reason;
    private final long issuedAt, duration;

    private UUID revoker;
    private String revokerName, revokedReason;
    private long revokedAt;

    public Grant(IUser user, IRank rank, IUser issuer, String reason, long issuedAt, long duration) {
        this.uniqueID = UUID.randomUUID();
        this.rankId = rank.getUniqueID();
        this.user = user.getUniqueId();
        this.issuer = issuer.getUniqueId();
        this.issuerName = issuer.getRealName();
        this.reason = reason;
        this.issuedAt = issuedAt;
        this.duration = duration;
    }

    public static Grant of(Document document) {
        return GsonProvider.GSON.fromJson(document.toJson(), Grant.class);
    }

    @Override
    public UUID getUniqueID() {
        return uniqueID;
    }

    @Override
    public IRank getRank() {
        return Maid.INSTANCE.getRankManager().getRanks().get(rankId);
    }

    @Override
    public UUID getUser() {
        return user;
    }

    @Override
    public UUID getIssuer() {
        return issuer;
    }

    @Override
    public String getIssuerName() {
        return issuerName;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public long getIssuedAt() {
        return issuedAt;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public UUID getRevoker() {
        return revoker;
    }

    @Override
    public String getRevokerName() {
        return revokerName;
    }

    @Override
    public String getRevokedReason() {
        return revokedReason;
    }

    @Override
    public long getRevokedAt() {
        return revokedAt;
    }

    @Override
    public boolean isActive() {
        if (duration == TimeUtil.PERMANENT) {
            return true;
        }
        return (issuedAt + duration) > System.currentTimeMillis();
    }

    public void revoke(User revoker, String revokedReason) {
        this.revoker = revoker.getUniqueId();
        this.revokerName = revoker.getRealName();
        this.revokedReason = revokedReason;
        this.revokedAt = System.currentTimeMillis();
    }
}

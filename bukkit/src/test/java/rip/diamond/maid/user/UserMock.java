package rip.diamond.maid.user;

import lombok.Getter;
import lombok.Setter;
import rip.diamond.maid.api.user.IGrant;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.player.User;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.RankManager;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserMock extends User {
    private transient final RankManager rankManager;
    private transient final PunishmentManager punishmentManager;

    public UserMock(UUID uniqueID, RankManager rankManager, PunishmentManager punishmentManager) {
        super(uniqueID);
        this.rankManager = rankManager;
        this.punishmentManager = punishmentManager;
    }

    @Override
    public void updateLastServer() {
        this.lastServer = "testEnvironment";
    }

    @Override
    public Set<IRank> getRanks() {
        Set<IRank> ranks = getActiveGrants().stream().map(IGrant::getRank).sorted(Comparator.comparingInt(IRank::getPriority).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
        ranks.add(rankManager.getDefaultRank());
        return ranks;
    }

    @Override
    public IRank getRank() {
        if (disguise != null) {
            return rankManager.getRanks().get(disguise.getDisguiseRankUUID());
        }
        return getRealRank();
    }

    @Override
    public List<IPunishment> getPunishments() {
        List<IPunishment> toReturn = new ArrayList<>();
        for (IPunishment punishment : punishmentManager.getPunishments()) {
            if (punishment.getUser().equals(uniqueID)) {
                toReturn.add(punishment);
            }
            if (punishment.getType() == IPunishment.PunishmentType.IP_BAN) {
                for (UUID alt : alts) {
                    if (punishment.getUser().equals(uniqueID) || punishment.getUser().equals(alt)) {
                        toReturn.add(punishment);
                    }
                }
            }
        }
        return toReturn;
    }
}

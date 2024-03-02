package rip.diamond.maid.user;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import lombok.Getter;
import lombok.Setter;
import rip.diamond.maid.api.user.*;
import rip.diamond.maid.api.user.permission.Permission;
import rip.diamond.maid.api.user.permission.UserPermission;
import rip.diamond.maid.chat.ChatRoom;
import rip.diamond.maid.disguise.Disguise;
import rip.diamond.maid.grant.Grant;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.RankManager;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserMock implements IUser {

    private final UUID uniqueID;
    private String name = "$undefined", texture = "";
    private long firstSeen = -1, lastSeen = -1;
    private String lastServer, ip = "Not Recorded";
    private final Set<String> ipHistory = new HashSet<>();
    private final Set<UUID> alts = new HashSet<>();
    private final Set<UserPermission> permissions = new HashSet<>();
    private final List<Grant> grants = new ArrayList<>();
    private Disguise disguise;
    private ChatRoom chatRoom = new ChatRoom();
    private Map<UserSettings, String> settings = new HashMap<>();

    private RankManager rankManager;
    private PunishmentManager punishmentManager;

    public UserMock(PlayerMock player, RankManager rankManager, PunishmentManager punishmentManager) {
        this.uniqueID = player.getUniqueId();
        this.name = player.getName();
        this.rankManager = rankManager;
        this.punishmentManager = punishmentManager;
    }

    public String getName() {
        if (disguise != null) {
            return disguise.getName();
        }
        return name;
    }

    @Override
    public String getRealName() {
        return name;
    }

    @Override
    public void setRealName(String name) {
        this.name = name;
    }

    @Override
    public String getSimpleDisplayName(boolean disguise) {
        if (disguise) {
            IRank rank = getRank();
            return "<" + rank.getColor() + ">" + getName();
        } else {
            IRank rank = getRealRank();
            return "<" + rank.getColor() + ">" + getRealName();
        }
    }

    @Override
    public String getDisplayName(boolean disguise) {
        if (disguise) {
            IRank rank = getRank();
            return rank.getPrefix() + getName() + rank.getSuffix();
        } else {
            IRank rank = getRealRank();
            return rank.getPrefix() + getRealName() + rank.getSuffix();
        }
    }

    @Override
    public void updateSeen() {
        if (this.firstSeen == -1) {
            this.firstSeen = System.currentTimeMillis();
        }
        this.lastSeen = System.currentTimeMillis();
    }
    @Override
    public void updateLastServer() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String getIP() {
        return ip;
    }

    @Override
    public void setIP(String ip) {
        this.ip = ip;
        this.ipHistory.add(ip);
    }

    @Override
    public Set<String> getIPHistory() {
        return ipHistory;
    }

    @Override
    public boolean containPermission(String permission, boolean includeRank) {
        if (includeRank) {
            return getAllPermissions().stream().anyMatch(userPermission -> userPermission.get().equalsIgnoreCase(permission));
        } else {
            return this.permissions.stream().anyMatch(userPermission -> userPermission.get().equalsIgnoreCase(permission));
        }
    }

    @Override
    public void addPermission(String permission) {
        this.permissions.add(new UserPermission(permission));
    }

    @Override
    public void removePermission(String permission) {
        this.permissions.removeIf(userPermission -> userPermission.get().equalsIgnoreCase(permission));
    }

    @Override
    public Set<Permission> getAllPermissions() {
        LinkedHashSet<Permission> permissions = new LinkedHashSet<>(getPermissions());
        for (IRank rank : getRanks()) {
            permissions.addAll(rank.getAllPermissions());
        }
        return permissions;
    }

    @Override
    public List<? extends IGrant> getActiveGrants() {
        return grants.stream().filter(IGrant::isActive).toList();
    }

    @Override
    public void addGrant(IGrant grant) {
        grants.add((Grant) grant);
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
    public IRank getRealRank() {
        return getRanks().stream().max(Comparator.comparingInt(IRank::getPriority)).orElseThrow(() -> new NoSuchElementException("Cannot find the highest priority rank based on grants"));
    }

    @Override
    public void setDisguise(IDisguise disguise) {
        this.disguise = (Disguise) disguise;
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

    @Override
    public List<IPunishment> getPunishments(List<IPunishment.PunishmentType> types) {
        return getPunishments().stream().filter(punishment -> types.contains(punishment.getType())).toList();
    }

    @Override
    public List<IPunishment> getActivePunishments() {
        return getPunishments().stream().filter(IPunishment::isActive).toList();
    }

    @Override
    public List<IPunishment> getActivePunishments(List<IPunishment.PunishmentType> types) {
        return getPunishments(types).stream().filter(IPunishment::isActive).toList();
    }
}

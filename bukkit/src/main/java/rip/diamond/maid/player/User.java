package rip.diamond.maid.player;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IGrant;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.permission.Permission;
import rip.diamond.maid.api.user.permission.UserPermission;
import rip.diamond.maid.grant.Grant;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class User implements IUser {

    public static User CONSOLE = new User(UUID.fromString("00000000-0000-0000-0000-000000000000"), "控制台", new UserPermission("*"));

    @SerializedName("_id")
    private final UUID uniqueID;
    private String name = "$undefined";
    private long firstSeen = -1, lastSeen = -1;
    private String lastServer, ip = "Not Recorded";
    private final Set<String> ipHistory = new HashSet<>();
    private final Set<UserPermission> permissions = new HashSet<>();
    private final List<Grant> grants = new ArrayList<>();

    public User(UUID uniqueID) {
        this.uniqueID = uniqueID;
    }

    private User(UUID uniqueID, String name, UserPermission... permissions) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.permissions.addAll(List.of(permissions));
    }

    public static User of(Document document) {
        return GsonProvider.GSON.fromJson(document.toJson(), User.class);
    }

    public String getName() {
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
    public String getDisplayName() {
        IRank rank = getRealRank();
        return rank.getPrefix() + getRealName() + rank.getSuffix();
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
        this.lastServer = MaidAPI.INSTANCE.getPlatform().getServerID();
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
    public boolean containPermission(String permission) {
        return this.permissions.stream().anyMatch(userPermission -> userPermission.get().equalsIgnoreCase(permission));
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
        Set<Permission> permissions = new HashSet<>(getPermissions());
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
        Maid.INSTANCE.getUserManager().saveUser(this);
    }

    @Override
    public Set<IRank> getRanks() {
        return getActiveGrants().stream().map(IGrant::getRank).collect(Collectors.toSet());
    }

    @Override
    public IRank getRank() {
        return getRealRank();
    }

    @Override
    public IRank getRealRank() {
        return getRanks().stream().max(Comparator.comparingInt(IRank::getPriority)).orElseThrow(() -> new NoSuchElementException("Cannot find the highest priority rank based on grants"));
    }

}

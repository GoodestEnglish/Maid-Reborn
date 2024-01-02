package rip.diamond.maid.player;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IGrant;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.grant.Grant;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.*;

@Getter
@Setter
public class User implements IUser {

    public static User CONSOLE = new User(UUID.fromString("00000000-0000-0000-0000-000000000000"), "控制台", "*");

    @SerializedName("_id")
    private final UUID uniqueID;
    private String name = "$undefined";
    private long firstSeen, lastSeen = -1;
    private String lastServer, ip = "Not Recorded";
    private final Set<String> ipHistory = new HashSet<>();
    private final Set<String> permissions = new HashSet<>();
    private final List<Grant> grants = new ArrayList<>();

    public User(UUID uniqueID) {
        this.uniqueID = uniqueID;
    }

    private User(UUID uniqueID, String name, String... permissions) {
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
    public void addGrant(IGrant grant) {
        grants.add((Grant) grant);
        Maid.INSTANCE.getUserManager().saveUser(this);
    }

}

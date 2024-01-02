package rip.diamond.maid.player;

import com.google.gson.annotations.SerializedName;
import org.bson.Document;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.*;

public class User implements IUser {

    @SerializedName("_id")
    private final UUID uniqueID;
    private String name = "$undefined";
    private long firstSeen, lastSeen = -1;
    private String lastServer, ip = "Not Recorded";
    private final Set<String> ipHistory = new HashSet<>();

    public User(UUID uniqueID) {
        this.uniqueID = uniqueID;
    }

    public static User of(Document document) {
        return GsonProvider.GSON.fromJson(document.toJson(), User.class);
    }

    @Override
    public UUID getUniqueId() {
        return uniqueID;
    }

    @Override
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
    public long getFirstSeen() {
        return firstSeen;
    }

    @Override
    public long getLastSeen() {
        return lastSeen;
    }

    @Override
    public void updateSeen() {
        if (this.firstSeen == -1) {
            this.firstSeen = System.currentTimeMillis();
        }
        this.lastSeen = System.currentTimeMillis();
    }

    @Override
    public String getLastServer() {
        return lastServer;
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
}

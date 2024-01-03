package rip.diamond.maid.rank;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.permission.RankPermission;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.*;

@Getter
@Setter
public class Rank implements IRank {

    @SerializedName("_id")
    private final UUID uniqueID;
    private String color, name, displayName;
    private String prefix = "", suffix = "";
    private boolean default_ = false;
    private int priority = 0;
    private final Set<RankPermission> permissions = new HashSet<>();
    private final List<UUID> parents = new ArrayList<>();

    public Rank(String color, String name) {
        this.uniqueID = UUID.randomUUID();
        this.color = color;
        this.name = name;
        this.displayName = name;
    }

    public static Rank of(Document document) {
        return GsonProvider.GSON.fromJson(document.toJson(), Rank.class);
    }

    @Override
    public String getDisplayName() {
        return color + displayName;
    }

    @Override
    public boolean isDefault() {
        return default_;
    }

    @Override
    public void setDefault(boolean default_) {
        this.default_ = default_;
    }

    @Override
    public Set<RankPermission> getAllPermissions() {
        Set<RankPermission> permissions = new HashSet<>(getPermissions());
        for (UUID parentUUID : parents) {
            IRank rank = Maid.INSTANCE.getRankManager().getRanks().get(parentUUID);
            permissions.addAll(rank.getAllPermissions());
        }
        return permissions;
    }

    @Override
    public void addPermission(String permission) {
        this.permissions.add(new RankPermission(permission, this));
    }

    @Override
    public void removePermission(String permission) {
        this.permissions.removeIf(rankPermission -> rankPermission.get().equalsIgnoreCase(permission));
    }

    @Override
    public void addParent(UUID parent) {
        this.parents.add(parent);
    }

    @Override
    public void removeParent(UUID parent) {
        this.parents.remove(parent);
    }
}

package rip.diamond.maid.rank;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.permission.RankPermission;
import rip.diamond.maid.util.HexColorUtil;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Rank implements IRank {

    @SerializedName("_id")
    private final UUID uniqueID;
    private String color, name, displayName;
    private String prefix = "", suffix = "", chatColor = "#FFFFFF";
    private boolean default_ = false;
    private int priority = 0;
    private final Set<RankPermission> permissions = new HashSet<>();
    private final Set<UUID> parents = new HashSet<>();

    public Rank(String color, String name) {
        this.uniqueID = UUID.randomUUID();
        this.color = color;
        this.name = name;
        this.displayName = name;
    }

    public static Rank of(Document document) {
        return GsonProvider.GSON.fromJson(document.toJson(), Rank.class);
    }

    public void setColor(String color) {
        if (!HexColorUtil.isHexColor(color)) {
            throw new IllegalArgumentException("The provided string '" + color + "' isn't a hex color");
        }
        this.color = color;
    }

    @Override
    public String getDisplayName(boolean withColor) {
        return (withColor ? "<" + color + ">" : "") + displayName;
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
        LinkedHashSet<RankPermission> permissions = new LinkedHashSet<>(getPermissions());
        for (UUID parentUUID : parents) {
            IRank rank = Maid.INSTANCE.getRankManager().getRanks().get(parentUUID);
            permissions.addAll(rank.getAllPermissions());
        }

        return permissions.stream().sorted(new Comparator<>() {
            @Override
            public int compare(RankPermission o1, RankPermission o2) {
                IRank r1 = Maid.INSTANCE.getRankManager().getRanks().get(o1.getAssociatedRankUUID());
                IRank r2 = Maid.INSTANCE.getRankManager().getRanks().get(o2.getAssociatedRankUUID());
                return Integer.compare(r2.getPriority(), r1.getPriority());
            }
        }).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public boolean containPermission(String permission) {
        return this.permissions.stream().anyMatch(rankPermission -> rankPermission.get().equalsIgnoreCase(permission));
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
    public boolean containParent(UUID parent) {
        return this.parents.stream().anyMatch(uuid -> uuid.equals(parent));
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

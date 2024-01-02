package rip.diamond.maid.rank;

import com.google.gson.annotations.SerializedName;
import org.bson.Document;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.UUID;

public class Rank implements IRank {

    @SerializedName("_id")
    private final UUID uniqueID;
    private String name, displayName;
    private String prefix, suffix = "";
    private boolean default_ = false;

    public Rank(String name) {
        this.uniqueID = UUID.randomUUID();
        this.name = name;
        this.displayName = name;
    }

    public static Rank of(Document document) {
        return GsonProvider.GSON.fromJson(document.toJson(), Rank.class);
    }

    @Override
    public UUID getUniqueID() {
        return uniqueID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean isDefault() {
        return default_;
    }

    @Override
    public void setDefault(boolean default_) {
        this.default_ = default_;
    }
}

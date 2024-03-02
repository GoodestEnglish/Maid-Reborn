package rip.diamond.maid.rank;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.mongo.MongoManager;
import rip.diamond.maid.redis.packets.bukkit.RankUpdatePacket;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.extend.MaidManager;
import rip.diamond.maid.util.json.GsonProvider;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RankManager extends MaidManager {

    private final IMaidAPI api;
    private final MongoManager mongoManager;

    @Getter private final Map<UUID, IRank> ranks = new ConcurrentHashMap<>();

    public RankManager(IMaidAPI api, MongoManager mongoManager) {
        this.api = api;
        this.mongoManager = mongoManager;

        //Load all the rank from database
        for (Document document : mongoManager.getRanks().find()) {
            Rank rank = Rank.of(document);
            ranks.put(rank.getUniqueID(), rank);
        }

        //Check how many default rank is in the cache
        long defaultRanks = ranks.values().stream().filter(IRank::isDefault).count();
        if (defaultRanks == 0) {
            //If there's no default rank, create one and store into database and cache
            Rank rank = new Rank("#D3D3D3", "Default");
            rank.setPrefix(CC.GRAY.toString());
            rank.setDisplayName("預設職階");
            rank.setDefault(true);

            saveRank(rank);
        }
    }

    public void saveRank(IRank rank) {
        plugin.getMongoManager().getRanks().replaceOne(Filters.eq("_id", rank.getUniqueID().toString()), Document.parse(GsonProvider.GSON.toJson(rank)), new ReplaceOptions().upsert(true));
        ranks.put(rank.getUniqueID(), rank);
        api.getPacketHandler().send(new RankUpdatePacket(Maid.API.getPlatform().getServerID(), (Rank) rank, false));
    }

    public void deleteRank(IRank rank) {
        plugin.getMongoManager().getRanks().deleteOne(Filters.eq("_id", rank.getUniqueID().toString()));
        ranks.remove(rank.getUniqueID());
        api.getPacketHandler().send(new RankUpdatePacket(Maid.API.getPlatform().getServerID(), (Rank) rank, true));
    }

    public IRank getDefaultRank() {
        return ranks.values().stream().filter(IRank::isDefault).findFirst().orElseThrow(() -> new NoSuchElementException("Cannot find default rank in cache"));
    }

    public List<IRank> getRanksInOrder() {
        return ranks.values().stream().sorted(Comparator.comparingInt(IRank::getPriority).reversed()).toList();
    }

    public @Nullable IRank getRank(String name) {
        return ranks.values().stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

}

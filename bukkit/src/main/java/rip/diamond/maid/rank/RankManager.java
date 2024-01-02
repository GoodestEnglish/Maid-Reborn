package rip.diamond.maid.rank;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.RankUpdatePacket;
import rip.diamond.maid.util.extend.MaidManager;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RankManager extends MaidManager {

    @Getter private final Map<UUID, IRank> ranks = new ConcurrentHashMap<>();

    public RankManager() {
        //Load all the rank from database
        for (Document document : plugin.getMongoManager().getRanks().find()) {
            Rank rank = Rank.of(document);
            ranks.put(rank.getUniqueID(), rank);
        }

        //Check how many default rank is in the cache
        long defaultRanks = ranks.values().stream().filter(IRank::isDefault).count();
        if (defaultRanks == 0) {
            //If there's no default rank, create one and store into database and cache
            Rank rank = new Rank("Default");
            rank.setPrefix("<gray>");
            rank.setDisplayName("預設");
            rank.setDefault(true);

            saveRank(rank);
        } else if (defaultRanks >= 2) {
            //If there's two or above, throw an error and stop the server
            Bukkit.shutdown();
            throw new RuntimeException("Cannot contain " + defaultRanks + " default ranks in database and cache");
        }
    }

    public void saveRank(IRank rank) {
        plugin.getMongoManager().getRanks().replaceOne(Filters.eq("_id", rank.getUniqueID().toString()), Document.parse(GsonProvider.GSON.toJson(rank)), new ReplaceOptions().upsert(true));
        ranks.put(rank.getUniqueID(), rank);
        PacketHandler.send(new RankUpdatePacket((Rank) rank, false));
    }

    public void deleteRank(IRank rank) {
        plugin.getMongoManager().getRanks().deleteOne(Filters.eq("_id", rank.getUniqueID().toString()));
        ranks.remove(rank.getUniqueID());
        PacketHandler.send(new RankUpdatePacket((Rank) rank, true));
    }

}

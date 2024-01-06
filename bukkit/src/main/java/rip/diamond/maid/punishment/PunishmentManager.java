package rip.diamond.maid.punishment;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.PunishmentUpdatePacket;
import rip.diamond.maid.util.Tasks;
import rip.diamond.maid.util.extend.MaidManager;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.ArrayList;
import java.util.List;

public class PunishmentManager extends MaidManager {

    @Getter private final List<IPunishment> punishments = new ArrayList<>();

    public PunishmentManager() {
        loadPunishments();
    }

    private void loadPunishments() {
        for (Document document : plugin.getMongoManager().getPunishments().find()) {
            IPunishment punishment = Punishment.of(document);
            punishments.add(punishment);
        }

        // TODO: 5/1/2024 Possibly sort punishments to make get method faster?
    }

    public void updatePunishment(IPunishment punishment) {
        punishments.removeIf(punishment_ -> punishment_.getUniqueID().equals(punishment.getUniqueID()));
        punishments.add(punishment);

        Tasks.runAsync(() -> {
            plugin.getMongoManager().getPunishments().replaceOne(Filters.eq("_id", punishment.getUniqueID()), Document.parse(GsonProvider.GSON.toJson(punishment)), new ReplaceOptions().upsert(true));
            PacketHandler.send(new PunishmentUpdatePacket(MaidAPI.INSTANCE.getPlatform().getServerID(), (Punishment) punishment));
        });
    }

}

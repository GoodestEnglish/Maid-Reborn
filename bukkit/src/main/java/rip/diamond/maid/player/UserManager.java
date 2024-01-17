package rip.diamond.maid.player;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.UserSettings;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.ProfileUpdatePacket;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.util.Tasks;
import rip.diamond.maid.util.extend.MaidManager;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager extends MaidManager {

    /**
     * This HashMap stores every logged-in users in this server session. Logout will not remove specific user from this map
     */
    @Getter private final ConcurrentHashMap<UUID, IUser> users = new ConcurrentHashMap<>();

    public CompletableFuture<Boolean> hasUser(UUID uniqueID) {
        return CompletableFuture.supplyAsync(() -> {
            Document document = plugin.getMongoManager().getUsers().find(Filters.eq("_id", uniqueID.toString())).first();
            if (document == null) {
                return false;
            }
            users.put(uniqueID, User.of(document));
            return true;
        });
    }

    public CompletableFuture<IUser> getUser(UUID uniqueID) {
        //If the user is found in the cache, simply get it from cache
        if (users.containsKey(uniqueID)) {
            return CompletableFuture.completedFuture(users.get(uniqueID));
        }

        //Otherwise, load the player from database and return the user
        return CompletableFuture.supplyAsync(() -> {
            Document document = plugin.getMongoManager().getUsers().find(Filters.eq("_id", uniqueID.toString())).first();
            User user = document == null ? new User(uniqueID) : User.of(document);

            users.put(uniqueID, user);

            return user;
        });
    }

    public void saveUser(IUser user) {
        Tasks.runAsync(() -> {
            plugin.getMongoManager().getUsers().replaceOne(Filters.eq("_id", user.getUniqueID().toString()), Document.parse(GsonProvider.GSON.toJson(user)), new ReplaceOptions().upsert(true));
            PacketHandler.send(new ProfileUpdatePacket(MaidAPI.INSTANCE.getPlatform().getServerID(), (User) user));
        });
    }

    public boolean isOn(UUID uuid, UserSettings settings) {
        if (!users.containsKey(uuid)) {
            throw new RuntimeException("Cannot find user with uuid " + uuid.toString() + " when checking for settings " + settings.name());
        }
        IUser user = users.get(uuid);
        return isOn(user, settings);
    }

    public boolean isOn(IUser user, UserSettings settings) {
        GlobalUser globalUser = GlobalUser.of(user);
        return isOn(globalUser, settings);
    }

    public boolean isOn(GlobalUser globalUser, UserSettings settings) {
        String value = globalUser.getSettings().get(settings);
        if (value == null) {
            return false;
        }
        return value.equals("開啟");
    }
}

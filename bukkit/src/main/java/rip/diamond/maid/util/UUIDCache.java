package rip.diamond.maid.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UUIDCache {

    public static final Map<UUID, String> UUID_USERNAME = new HashMap<>();
    public static final Map<String, UUID> USERNAME_UUID = new HashMap<>();

    public static void insert(UUID uuid, String username) {
        UUID_USERNAME.put(uuid, username);
        USERNAME_UUID.put(username, uuid);
    }

    public static UUID getUUID(String username) {
        UUID uuid = USERNAME_UUID.get(username);
        if (uuid != null) {
            return uuid;
        }

        //UUID not found in the cache, fetch it asynchronously
        CompletableFuture<OfflinePlayer> offlinePlayerFuture = getOfflinePlayer(username);
        return offlinePlayerFuture.thenApplyAsync(OfflinePlayer::getUniqueId).join();
    }

    public static String getUsername(UUID uuid) {
        String username = UUID_USERNAME.get(uuid);
        if (username != null) {
            return username;
        }

        //Username not found in the cache, fetch it asynchronously
        CompletableFuture<OfflinePlayer> offlinePlayerFuture = getOfflinePlayer(uuid);
        return offlinePlayerFuture.thenApplyAsync(OfflinePlayer::getName).join();
    }

    private static CompletableFuture<OfflinePlayer> getOfflinePlayer(String username) {
        return CompletableFuture.supplyAsync(() -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
            insert(offlinePlayer.getUniqueId(), offlinePlayer.getName());
            return offlinePlayer;
        });
    }

    private static CompletableFuture<OfflinePlayer> getOfflinePlayer(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            insert(offlinePlayer.getUniqueId(), offlinePlayer.getName());
            return offlinePlayer;
        });
    }
}


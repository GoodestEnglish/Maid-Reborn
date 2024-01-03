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

    public static CompletableFuture<UUID> getUUID(String username) {
        UUID cachedUUID = USERNAME_UUID.get(username);
        if (cachedUUID != null) {
            return CompletableFuture.completedFuture(cachedUUID);
        }

        // UUID not found in the cache, fetch it asynchronously
        CompletableFuture<OfflinePlayer> offlinePlayerFuture = getOfflinePlayer(username);

        // Use thenCompose to chain CompletableFuture
        return offlinePlayerFuture.thenComposeAsync(offlinePlayer -> {
            UUID fetchedUuid = offlinePlayer.getUniqueId();

            // Insert into the cache to ensure consistency
            insert(fetchedUuid, offlinePlayer.getName());

            return CompletableFuture.completedFuture(fetchedUuid);
        });
    }

    public static CompletableFuture<String> getUsername(UUID uuid) {
        String cachedUsername = UUID_USERNAME.get(uuid);
        if (cachedUsername != null) {
            return CompletableFuture.completedFuture(cachedUsername);
        }

        // UUID not found in the cache, fetch it asynchronously
        CompletableFuture<OfflinePlayer> offlinePlayerFuture = getOfflinePlayer(uuid);

        // Use thenCompose to chain CompletableFuture
        return offlinePlayerFuture.thenComposeAsync(offlinePlayer -> {
            String fetchedUsername = offlinePlayer.getName();

            // Insert into the cache to ensure consistency
            insert(offlinePlayer.getUniqueId(), fetchedUsername);

            return CompletableFuture.completedFuture(fetchedUsername);
        });
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


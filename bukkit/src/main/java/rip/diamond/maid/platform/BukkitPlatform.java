package rip.diamond.maid.platform;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.api.server.IPlatform;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.config.Config;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.task.ITaskRunner;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BukkitPlatform implements IPlatform {

    private final ITaskRunner task;

    @Override
    public Platform getPlatform() {
        return Platform.BUKKIT;
    }

    @Override
    public String getServerID() {
        return Config.SERVER_ID.toString();
    }

    @Override
    public void log(String... s) {
        Common.log(s);
    }

    @Override
    public void run(Runnable runnable) {
        task.run(runnable);
    }

    @Override
    public void runLater(Runnable runnable, long delay) {
        task.runLater(runnable, delay);
    }

    @Override
    public void runTimer(Runnable runnable, long delay, long interval) {
        task.runTimer(runnable, delay, interval);
    }

    @Override
    public void runAsync(Runnable runnable) {
        task.runAsync(runnable);
    }

    @Override
    public void runAsyncLater(Runnable runnable, long delay) {
        task.runAsyncLater(runnable, delay);
    }

    @Override
    public void runAsyncTimer(Runnable runnable, long delay, long interval) {
        task.runAsyncTimer(runnable, delay, interval);
    }

    @Override
    public void sendMessage(UUID uuid, String permission, String... messages) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        if (permission != null && !player.hasPermission(permission)) {
            return;
        }
        if (messages.length == 0) {
            throw new RuntimeException("Message length is 0 (Forgot to set a permission?)");
        }
        for (String message : messages) {
            player.sendMessage(Common.text(message));
        }
    }

    @Override
    public void broadcastMessage(String permission, String... messages) {
        if (messages.length == 0) {
            throw new RuntimeException("Message length is 0 (Forgot to set a permission?)");
        }
        Common.broadcastPermissionMessage(permission, messages);
    }

    @Override
    public boolean isWhitelisted() {
        return Bukkit.getServer().hasWhitelist();
    }

    @Override
    public List<String> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    @Override
    public int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }
}

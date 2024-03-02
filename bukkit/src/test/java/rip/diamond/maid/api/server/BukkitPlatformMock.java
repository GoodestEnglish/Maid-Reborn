package rip.diamond.maid.api.server;

import be.seeseemelk.mockbukkit.ServerMock;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BukkitPlatformMock implements IPlatform {

    private final ServerMock server;

    @Override
    public Platform getPlatform() {
        return Platform.BUKKIT;
    }

    @Override
    public String getServerID() {
        return "testEnvironment";
    }

    @Override
    public void log(String... s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void run(Runnable runnable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void runLater(Runnable runnable, long delay) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void runTimer(Runnable runnable, long delay, long interval) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void runAsync(Runnable runnable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void runAsyncLater(Runnable runnable, long delay) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void runAsyncTimer(Runnable runnable, long delay, long interval) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void sendMessage(UUID uuid, String permission, String... messages) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void broadcastMessage(String permission, String... messages) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean isWhitelisted() {
        return server.hasWhitelist();
    }

    @Override
    public List<String> getOnlinePlayers() {
        return server.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    @Override
    public int getMaxPlayers() {
        return server.getMaxPlayers();
    }

}

package rip.diamond.maid.api.server;

import java.util.List;
import java.util.UUID;

public interface IPlatform {

    Platform getPlatform();

    String getServerID();

    void log(String... s);

    void run(Runnable runnable);

    void runLater(Runnable runnable, long delay);

    void runTimer(Runnable runnable, long delay, long interval);

    void runAsync(Runnable runnable);

    void runAsyncLater(Runnable runnable, long delay);

    void runAsyncTimer(Runnable runnable, long delay, long interval);

    void sendMessage(UUID uuid, String permission, String... messages);

    void broadcastMessage(String permission, String... messages);

    boolean isWhitelisted();

    List<String> getOnlinePlayers();

    int getMaxPlayers();

}

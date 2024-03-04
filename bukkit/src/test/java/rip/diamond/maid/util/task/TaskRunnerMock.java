package rip.diamond.maid.util.task;

import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

//All bukkit task should be running sync in unit testing, to prevent MockBukkit#unmock run before the task ends
@RequiredArgsConstructor
public class TaskRunnerMock implements ITaskRunner {

    private final ServerMock server;
    private final MockPlugin plugin;

    public void run(Runnable runnable, boolean async) {
        runnable.run();
    }

    //In MockBukkit, BukkitSchedulerMock.runTask by default do runTaskLater(1L), we don't want the 1 tick delay happen
    public BukkitTask run(Runnable runnable) {
        runnable.run();
        return null;
    }

    public BukkitTask runAsync(Runnable runnable) {
        runnable.run();
        return null;
    }

    public BukkitTask runLater(Runnable runnable, long delay) {
        return server.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public BukkitTask runAsyncLater(Runnable runnable, long delay) {
        return server.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public BukkitTask runTimer(Runnable runnable, long delay, long interval) {
        return server.getScheduler().runTaskTimer(plugin, runnable, delay, interval);
    }

    public BukkitTask runAsyncTimer(Runnable runnable, long delay, long interval) {
        return server.getScheduler().runTaskTimer(plugin, runnable, delay, interval);
    }

    public BukkitScheduler getScheduler() {
        return server.getScheduler();
    }

}

package rip.diamond.maid.util.task;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import rip.diamond.maid.Maid;
import rip.diamond.maid.util.task.ITaskRunner;

public class TaskRunnerAdapter implements ITaskRunner {

    public void run(Runnable runnable, boolean async) {
        if(async) {
            Maid.INSTANCE.getServer().getScheduler().runTaskAsynchronously(Maid.INSTANCE, runnable);
        } else {
            runnable.run();
        }
    }

    public BukkitTask run(Runnable runnable) {
        return Maid.INSTANCE.getServer().getScheduler().runTask(Maid.INSTANCE, runnable);
    }

    public BukkitTask runAsync(Runnable runnable) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskAsynchronously(Maid.INSTANCE, runnable);
    }

    public BukkitTask runLater(Runnable runnable, long delay) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskLater(Maid.INSTANCE, runnable, delay);
    }

    public BukkitTask runAsyncLater(Runnable runnable, long delay) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskLaterAsynchronously(Maid.INSTANCE, runnable, delay);
    }

    public BukkitTask runTimer(Runnable runnable, long delay, long interval) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskTimer(Maid.INSTANCE, runnable, delay, interval);
    }

    public BukkitTask runAsyncTimer(Runnable runnable, long delay, long interval) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskTimerAsynchronously(Maid.INSTANCE, runnable, delay, interval);
    }

    public BukkitScheduler getScheduler() {
        return Maid.INSTANCE.getServer().getScheduler();
    }
}

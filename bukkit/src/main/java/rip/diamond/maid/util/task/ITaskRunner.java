package rip.diamond.maid.util.task;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public interface ITaskRunner {

    void run(Runnable runnable, boolean async);

    BukkitTask run(Runnable runnable);

    BukkitTask runAsync(Runnable runnable);

    BukkitTask runLater(Runnable runnable, long delay);

    BukkitTask runAsyncLater(Runnable runnable, long delay);

    BukkitTask runTimer(Runnable runnable, long delay, long interval);

    BukkitTask runAsyncTimer(Runnable runnable, long delay, long interval);

    BukkitScheduler getScheduler();

}

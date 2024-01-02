package rip.diamond.maid.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import rip.diamond.maid.Maid;

import java.util.concurrent.ThreadFactory;

public class Tasks {

    public static ThreadFactory newThreadFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).build();
    }

    public static void run(Runnable runnable, boolean async) {
        if(async) {
            Maid.INSTANCE.getServer().getScheduler().runTaskAsynchronously(Maid.INSTANCE, runnable);
        } else {
            runnable.run();
        }
    }

    public static BukkitTask run(Runnable runnable) {
        return Maid.INSTANCE.getServer().getScheduler().runTask(Maid.INSTANCE, runnable);
    }

    public static BukkitTask runAsync(Runnable runnable) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskAsynchronously(Maid.INSTANCE, runnable);
    }

    public static BukkitTask runLater(Runnable runnable, long delay) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskLater(Maid.INSTANCE, runnable, delay);
    }

    public static BukkitTask runAsyncLater(Runnable runnable, long delay) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskLaterAsynchronously(Maid.INSTANCE, runnable, delay);
    }

    public static BukkitTask runTimer(Runnable runnable, long delay, long interval) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskTimer(Maid.INSTANCE, runnable, delay, interval);
    }

    public static BukkitTask runAsyncTimer(Runnable runnable, long delay, long interval) {
        return Maid.INSTANCE.getServer().getScheduler().runTaskTimerAsynchronously(Maid.INSTANCE, runnable, delay, interval);
    }

    public static BukkitScheduler getScheduler() {
        return Maid.INSTANCE.getServer().getScheduler();
    }
}

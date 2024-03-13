package rip.diamond.maid.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import rip.diamond.maid.Maid;

public abstract class TaskTicker extends BukkitRunnable {

    protected final Plugin plugin;
    public final Config config;

    public int tick;
    @Setter public boolean finishPreRun = false;
    @Setter public boolean stop = false;

    public TaskTicker(int delay, int period, boolean async) {
        this(Maid.INSTANCE, delay, period, async);
    }

    public TaskTicker(Plugin plugin, int delay, int period, boolean async) {
        this(plugin, new Config(delay, period, async));
    }

    public TaskTicker(Plugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
        if (config.async) {
            this.runTaskTimerAsynchronously(plugin, config.delay, config.period);
        } else {
            this.runTaskTimer(plugin, config.delay, config.period);
        }
    }

    @Override
    public void run() {
        if (!finishPreRun) {
            tick = getStartTick();
            preRun();
            finishPreRun = true;
        }
        if (stop) {
            return;
        }

        onRun();

        if (!isCancelled()) {
            if (getTickType() == TickType.COUNT_UP) {
                countUp();
            } else if (getTickType() == TickType.COUNT_DOWN) {
                countDown();
            }
        }
    }

    public abstract void onRun();

    public void preRun() {

    }

    public TickType getTickType() {
        return TickType.NONE;
    }

    public int getStartTick() {
        return 0;
    }

    public void countUp() {
        tick++;
    }

    public void countDown() {
        tick--;
    }

    public enum TickType {
        COUNT_UP,
        COUNT_DOWN,
        NONE
    }

    @RequiredArgsConstructor
    public static class Config {
        public final int delay;
        public final int period;
        public final boolean async;
    }

}
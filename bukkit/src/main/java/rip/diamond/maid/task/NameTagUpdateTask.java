package rip.diamond.maid.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.util.TaskTicker;

public class NameTagUpdateTask extends TaskTicker {
    public NameTagUpdateTask() {
        super(0, 2, true);
    }

    @Override
    public void onRun() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Maid.INSTANCE.getNameTagManager().sendNameTag(player);
        }
    }
}

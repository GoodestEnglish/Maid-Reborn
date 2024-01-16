package rip.diamond.maid.task;

import com.google.common.collect.ImmutableList;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.BroadcastPacket;
import rip.diamond.maid.util.Alert;
import rip.diamond.maid.util.TaskTicker;

/**
 * This class allows player to join the server after 5 seconds
 */
public class FinishStartupTask extends TaskTicker {
    public FinishStartupTask() {
        super(100, 0, false);
    }

    @Override
    public void onRun() {
        String serverID = MaidAPI.INSTANCE.getPlatform().getServerID();

        plugin.getServerManager().setLoaded(true);

        Alert alert = Alert.SERVER_STARTED;
        String message = alert.get(serverID);
        PacketHandler.send(new BroadcastPacket(serverID, alert.getType().getPermission(), ImmutableList.of(message)));

        cancel();
    }
}

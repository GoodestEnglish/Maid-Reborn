package rip.diamond.maid.task;

import com.google.common.collect.ImmutableList;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.Maid;
import rip.diamond.maid.redis.packets.bukkit.BroadcastPacket;
import rip.diamond.maid.util.Alert;
import rip.diamond.maid.util.TaskTicker;

/**
 * This class allows player to join the server after 5 seconds
 */
public class FinishStartupTask extends TaskTicker {

    private final IMaidAPI api;

    public FinishStartupTask(IMaidAPI api) {
        super(100, 0, false);
        this.api = api;
    }

    @Override
    public void onRun() {
        String serverID = Maid.API.getPlatform().getServerID();

        Maid.INSTANCE.getServerManager().setLoaded(true);

        Alert alert = Alert.SERVER_STARTED;
        String message = alert.get(serverID);
        api.getPacketHandler().send(new BroadcastPacket(serverID, alert.getType().getPermission(), ImmutableList.of(message)));

        cancel();
    }
}

package rip.diamond.maid.task;

import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.AlertPacket;
import rip.diamond.maid.util.Alert;
import rip.diamond.maid.util.CC;
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

        Maid.INSTANCE.getServerManager().setLoaded(true);
        PacketHandler.send(new AlertPacket(MaidAPI.INSTANCE.getPlatform().getServerID(), Alert.SERVER_STARTED, serverID));

        cancel();
    }
}

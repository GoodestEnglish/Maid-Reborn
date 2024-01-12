package rip.diamond.maid.redis.packets.bukkit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.Platform;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.server.Server;
import rip.diamond.maid.util.Preconditions;

@Getter
@RequiredArgsConstructor
public class ServerUpdatePacket implements Packet {

    private final String from;
    private final Server server;

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public String getTo() {
        return "server";
    }

    @Override
    public void onReceive() {
        Preconditions.checkArgument(MaidAPI.INSTANCE.getPlatform().getPlatform() == Platform.BUKKIT, getClass().getSimpleName() + " can only run in bukkit platform");

        //Only update the target server data when the server is loaded
        if (!Maid.INSTANCE.getServerManager().isLoaded()) {
            return;
        }

        Maid.INSTANCE.getServerManager().getServers().put(server.getID(), server);
    }
}

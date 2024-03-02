package rip.diamond.maid.util;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IUser;

public class MockUtil {

    public static PlayerMock addPlayer(ServerMock server, Maid plugin) {
        PlayerMock player = server.addPlayer();
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();

        return player;
    }

}

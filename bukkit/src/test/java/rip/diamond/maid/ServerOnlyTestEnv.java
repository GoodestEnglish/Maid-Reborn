package rip.diamond.maid;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMockFactory;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ServerOnlyTestEnv {
    protected ServerMock server;
    private PlayerMockFactory playerMockFactory;

    public PluginManagerMock getPluginManager() {
        return server.getPluginManager();
    }

    @BeforeEach
    public void setupServer() {
        unloadIfAlreadyMocking();
        server = MockBukkit.mock(new CustomVerServerMock());
        playerMockFactory = new PlayerMockFactory(server);
    }

    private void unloadIfAlreadyMocking() {
        if (MockBukkit.isMocked()) {
            MockBukkit.unmock();
        }
    }

    public PlayerMock createRandomPlayer() {
        return playerMockFactory.createRandomPlayer();
    }

    public WorldMock createSimpleWorld(String name) {
        return server.addSimpleWorld(name);
    }

    public PlayerMock createRandomOnlinePlayer() {
        PlayerMock player = createRandomPlayer();
        server.addPlayer(player);
        return player;
    }

    @AfterEach
    public void tearDownServer() {
        MockBukkit.unmock();
    }
}


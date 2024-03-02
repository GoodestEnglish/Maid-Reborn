package rip.diamond.maid.test.chat;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.*;
import rip.diamond.maid.Maid;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.MockUtil;

public class ChatTest {

    private ServerMock server;
    private Maid plugin;

    @BeforeEach
    public void setUp() {
        try {
            server = MockBukkit.mock();
            plugin = MockBukkit.load(Maid.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test if the player can chat when chatroom is muted")
    void testChatOutput() {
        PlayerMock player = MockUtil.addPlayer(server, plugin);

        plugin.getChatManager().setMuted(false);
        player.chat("test");
        Assertions.assertSame(player.nextMessage(), CC.RED + "聊天室暫時被關閉了, 你暫時無法再聊天室說話");
    }

}

package rip.diamond.maid.chat;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rip.diamond.maid.ServerOnlyTestEnv;
import rip.diamond.maid.test.chat.ChatConfigMock;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ChatListenerTest extends ServerOnlyTestEnv {

    private ChatListener chatListener;
    private ChatManager chatManager;

    private PlayerMock player;

    @BeforeEach
    void setUp() {
        chatManager = new ChatManager(new ChatConfigMock());
        chatListener = new ChatListener(chatManager);

        player = createRandomPlayer();
    }


    @Nested
    class ChatMuted {

        private AsyncChatEvent event;

        @BeforeEach
        void setUp() {
            event = new AsyncChatEvent(
                    true,
                    player,
                    new HashSet<>(),
                    null,
                    null,
                    null,
                    null
            );

        }

        @Test
        void byDefault_shouldAllowChat() {
            chatListener.onChatMute(event);
            assertFalse(event.isCancelled());
        }

        @Test
        void shouldCancel_IfMuted() {
            chatManager.setMuted(true);
            chatListener.onChatMute(event);

            assertTrue(event.isCancelled());
            assertEquals(
                    player.nextMessage(),
                    LegacyComponentSerializer.legacySection().serialize(Common.text(CC.RED + "聊天室暫時被關閉了, 你暫時無法再聊天室說話"))
            );
        }

    }


}
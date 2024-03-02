package rip.diamond.maid.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rip.diamond.maid.chat.ChatManager;
import rip.diamond.maid.config.ChatConfig;
import rip.diamond.maid.config.ChatConfigMock;

import static org.junit.jupiter.api.Assertions.*;

class ChatManagerTest {

    private ChatManager chatManager;
    private ChatConfig chatConfig;

    @BeforeEach
    void setUp() {
        chatConfig = new ChatConfigMock();
        chatManager = new ChatManager(chatConfig);
    }

    @Nested
    class Mute {
        @Test
        @DisplayName("Test if mute variable is successfully updated to true")
        void testUpdateMuteVariableToTrue() {
            chatManager.setMuted(true);

            assertTrue(chatConfig.isChatMuted());
            assertTrue(chatManager.isMuted());
        }

        @Test
        @DisplayName("Test if mute variable is successfully updated to false")
        void testUpdateMuteVariableToFalse() {
            chatManager.setMuted(false);

            assertFalse(chatConfig.isChatMuted());
            assertFalse(chatManager.isMuted());
        }
    }

    @Nested
    class Delay {
        @Test
        @DisplayName("Test if mute variable is successfully updated to 10")
        void testUpdateDelayVariableTo10() {
            chatConfig.setChatDelay(10);

            assertEquals(10, chatManager.getDelay());
            assertEquals(10, chatConfig.getChatDelay());
        }

        @Test
        @DisplayName("Test if delay variable is successfully updated to 20")
        void testUpdateDelayVariableTo20() {
            chatConfig.setChatDelay(20);

            assertEquals(20, chatManager.getDelay());
            assertEquals(20, chatConfig.getChatDelay());
        }
    }

}
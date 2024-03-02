package rip.diamond.maid.test.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rip.diamond.maid.chat.ChatManager;
import rip.diamond.maid.config.ChatConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChatManagerTest {


    /*
    1. mute -> change value
    2. (listener -> mute) -> mute.set = true, listener, cancel message
     */

    private ChatManager chatManager;
    private ChatConfig chatConfig;

    @BeforeEach
    void setUp() {
        chatConfig = new ChatConfigMock();
        chatManager = new ChatManager(chatConfig);
    }


    @Test
    void setMuted_shouldUpdateMutedValue() {
        chatManager.setMuted(true);

        assertTrue(chatConfig.isChatMuted());
        assertTrue(chatManager.isMuted());
    }


    @Nested
    class ChatDelay {

        @Test
        void shouldReturnValuesFromConfig() {
            chatConfig.setChatDelay(10);
            assertEquals(10, chatManager.getDelay());
        }

        @Test
        void shouldReturnOrUpdateValueFromConfig() {
            chatConfig.setChatDelay(20);

            assertEquals(20, chatManager.getDelay());
            assertEquals(20, chatConfig.getChatDelay());
        }
    }



}
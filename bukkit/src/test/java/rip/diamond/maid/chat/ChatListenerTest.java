package rip.diamond.maid.chat;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.chat.ChatRoomType;
import rip.diamond.maid.user.UserMock;
import rip.diamond.maid.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ChatListenerTest extends MaidTestEnvironment {

    private PlayerMock player;
    private IUser user;

    @BeforeEach
    void setUp() {
        loadManagersAndListeners();

        //Create a random player
        player = createRandomOnlinePlayer();
        
        //Create a default user profile
        user = new UserMock(player, rankManager, punishmentManager);
        userManager.getUsers().put(player.getUniqueId(), user);
    }
    
    @Nested
    class Chat {
        private AsyncChatEvent event;

        @BeforeEach
        void setUp() {
            event = EventUtil.newAsyncChatEvent(player, player);
        }

        @Test
        @DisplayName("Test chat format when the viewer is a player")
        void testChatFormatPlayer() {
            chatListener.onChatFormat(event);
            // TODO: 2/3/2024  
        }

        @Test
        @DisplayName("Test chat mute event default state")
        void testChatMuteDefault() {
            chatListener.onChatMute(event);
            assertFalse(event.isCancelled());
        }

        @Test
        @DisplayName("Test chat mute event is successfully cancelled")
        void testChatMuteCancel() {
            chatManager.setMuted(true);
            chatListener.onChatMute(event);

            assertTrue(event.isCancelled());
            assertEquals(player.nextMessage(), Common.legacy(CC.RED + "聊天室暫時被關閉了, 你暫時無法再聊天室說話"));
        }

        @Test
        @DisplayName("Test where is the chat message goes when player is in staff chat")
        void testChatMessagingStaffChat() {
            user.getChatRoom().setType(ChatRoomType.STAFF);
            player.addAttachment(plugin, MaidPermission.SETTINGS_STAFF_CHAT, true);

            chatListener.onChatMessaging(event);

            assertTrue(event.isCancelled());
            // TODO: 2/3/2024 Check if redis received the packet or not
        }

    }


}
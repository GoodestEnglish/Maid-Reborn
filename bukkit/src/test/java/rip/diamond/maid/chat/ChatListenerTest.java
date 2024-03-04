package rip.diamond.maid.chat;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.UserSettings;
import rip.diamond.maid.api.user.chat.ChatRoomType;
import rip.diamond.maid.disguise.Disguise;
import rip.diamond.maid.redis.messaging.IPacketHandler;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.redis.packets.bukkit.chat.StaffMessagePacket;
import rip.diamond.maid.user.UserMock;
import rip.diamond.maid.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class ChatListenerTest extends MaidTestEnvironment {

    private PlayerMock player;
    private IUser user;

    private PlayerMock viewer;
    private IUser userViewer;

    @BeforeEach
    void setUp() {
        loadManagersAndListeners();

        //Create random players
        player = createRandomOnlinePlayer();
        viewer = createRandomOnlinePlayer();

        //Create a default user profile
        user = new UserMock(player.getUniqueId(), rankManager, punishmentManager);
        userManager.getUsers().put(player.getUniqueId(), user);

        userViewer = new UserMock(viewer.getUniqueId(), rankManager, punishmentManager);
        userManager.getUsers().put(viewer.getUniqueId(), userViewer);
    }
    
    @Nested
    class Chat {
        private AsyncChatEvent event;

        @BeforeEach
        void setUp() {
            event = EventUtil.newAsyncChatEvent(player, player, viewer);
        }

        @Test
        @DisplayName("Test chat format when the viewer is a player")
        void testChatFormatPlayer() {
            chatListener.onChatFormat(event);
            assertEquals(
                    Common.legacy(event.renderer().render(player, player.displayName(), Common.text(EventUtil.DEFAULT_MESSAGE), player)),
                    Common.legacy(CC.GRAY + user.getDisplayName(true) + CC.GRAY + ":" + CC.RESET + " " + CC.WHITE + EventUtil.DEFAULT_MESSAGE)
            );
        }

        @Test
        @DisplayName("Test chat format when the viewer is a console")
        void testChatFormatConsole() {
            chatListener.onChatFormat(event);
            assertEquals(
                    Common.legacy(event.renderer().render(player, player.displayName(), Common.text(EventUtil.DEFAULT_MESSAGE), server.getConsoleSender())),
                    Common.legacy(CC.GRAY + user.getDisplayName(true) + CC.GRAY + ":" + CC.RESET + " " + CC.WHITE + EventUtil.DEFAULT_MESSAGE)
            );
        }

        @Test
        @DisplayName("Test chat format when the viewer is a console, but sender is disguised")
        void testChatFormatConsolePlayerDisguised() {
            user.setDisguise(new Disguise("GoodestEnglish", "GoodestEnglish", rankManager.getDefaultRank().getUniqueID()));

            chatListener.onChatFormat(event);
            assertEquals(
                    Common.legacy(event.renderer().render(player, player.displayName(), Common.text(EventUtil.DEFAULT_MESSAGE), server.getConsoleSender())),
                    Common.legacy("(真實名稱為 " + user.getRealName() + ") " + CC.GRAY + "GoodestEnglish" + CC.GRAY + ":" + CC.RESET + " " + CC.WHITE + EventUtil.DEFAULT_MESSAGE)
            );
        }

        @Test
        @DisplayName("Test chat mute event default state")
        void testChatMute1() {
            chatListener.onChatMute(event);
            assertFalse(event.isCancelled());
        }

        @Test
        @DisplayName("Test chat mute event is successfully cancelled")
        void testChatMute2() {
            chatManager.setMuted(true);
            chatListener.onChatMute(event);

            assertTrue(event.isCancelled());
            assertEquals(player.nextMessage(), Common.legacy(CC.RED + "聊天室暫時被關閉了, 你暫時無法再聊天室說話"));
        }

        @Test
        @DisplayName("Test where is the chat message goes when player is in staff chat")
        void testChatMessaging() {
            user.getChatRoom().setType(ChatRoomType.STAFF);
            player.addAttachment(plugin, MaidPermission.SETTINGS_STAFF_CHAT, true);
            chatListener.onChatMessaging(event);

            assertTrue(event.isCancelled());

            Packet packet = api.getPacket(StaffMessagePacket.class);
            assertNotNull(packet);
            verify(api.getJedis()).publish(IPacketHandler.CHANNEL, PacketUtil.encode(packet));
        }

        @Test
        @DisplayName("Test if the message is blocked successfully when global message is off")
        void testChatSettings1() {
            user.getSettings().put(UserSettings.GLOBAL_MESSAGE, "關閉");
            chatListener.onChatSettings(event);

            assertTrue(event.isCancelled());
            assertEquals(player.nextMessage(), Common.legacy(CC.RED + "你必須要在設定開啟聊天室才能發送訊息"));
        }

        @Test
        @DisplayName("Test if the message is sent successfully when global message is on")
        void testChatSettings2() {
            user.getSettings().put(UserSettings.GLOBAL_MESSAGE, "開啟");
            userViewer.getSettings().put(UserSettings.GLOBAL_MESSAGE, "開啟");
            chatListener.onChatSettings(event);

            assertFalse(event.isCancelled());
            assertTrue(event.viewers().contains(viewer));
        }

        @Test
        @DisplayName("Test if the message is sent successfully when global message is on, but viewer's global message is off")
        void testChatSettings3() {
            user.getSettings().put(UserSettings.GLOBAL_MESSAGE, "開啟");
            userViewer.getSettings().put(UserSettings.GLOBAL_MESSAGE, "關閉");
            chatListener.onChatSettings(event);

            assertFalse(event.isCancelled());
            assertFalse(event.viewers().contains(viewer));
        }
    }
}
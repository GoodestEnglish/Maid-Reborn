package rip.diamond.maid.test.chat;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.redis.messaging.IPacketHandler;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.redis.packets.bukkit.ProfileUpdatePacket;
import rip.diamond.maid.redis.packets.bukkit.chat.DirectMessagePacket;
import rip.diamond.maid.server.GlobalUser;
import rip.diamond.maid.user.UserMock;
import rip.diamond.maid.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class ChatManagerTest extends MaidTestEnvironment {

    @BeforeEach
    void setUp() {
        loadManagersAndListeners();
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

    @Nested
    class DirectMessage {
        private PlayerMock player1;
        private PlayerMock player2;
        private IUser user1;
        private IUser user2;

        @BeforeEach
        void setUp() {
            player1 = createRandomOnlinePlayer();
            player2 = createRandomOnlinePlayer();

            user1 = new UserMock(player1.getUniqueId(), rankManager, punishmentManager);
            userManager.getUsers().put(player1.getUniqueId(), user1);

            user2 = new UserMock(player2.getUniqueId(), rankManager, punishmentManager);
            userManager.getUsers().put(player2.getUniqueId(), user2);
        }

        @Test
        @DisplayName("Test if direct message is successfully sent from player1 to player2")
        void testSendDirectMessage1() {
            chatManager.sendDirectMessage(user1, GlobalUser.of(user2, api), EventUtil.DEFAULT_MESSAGE);

            Packet packet = api.getPacket(DirectMessagePacket.class);
            assertNotNull(packet);
            verify(api.getJedis()).publish(IPacketHandler.CHANNEL, PacketUtil.encode(packet));

            assertEquals(player1.nextMessage(), Common.legacy(CC.PINK + "➥ 給 " + user2.getSimpleDisplayName(false) + CC.WHITE + ": " + CC.GRAY + EventUtil.DEFAULT_MESSAGE));
        }

        @Test
        @DisplayName("Test if direct message is successfully sent from player2 to player1")
        void testSendDirectMessage2() {
            chatManager.sendDirectMessage(user2, GlobalUser.of(user1, api), EventUtil.DEFAULT_MESSAGE);

            Packet packet = api.getPacket(DirectMessagePacket.class);
            assertNotNull(packet);
            verify(api.getJedis()).publish(IPacketHandler.CHANNEL, PacketUtil.encode(packet));

            assertEquals(player2.nextMessage(), Common.legacy(CC.PINK + "➥ 給 " + user1.getSimpleDisplayName(false) + CC.WHITE + ": " + CC.GRAY + EventUtil.DEFAULT_MESSAGE));
        }
    }
}
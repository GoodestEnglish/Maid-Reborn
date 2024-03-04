package rip.diamond.maid.player;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.UserSettings;
import rip.diamond.maid.redis.messaging.IPacketHandler;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.user.UserMock;
import rip.diamond.maid.util.EventUtil;
import rip.diamond.maid.util.MaidTestEnvironment;
import rip.diamond.maid.util.PacketUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class UserListenerTest extends MaidTestEnvironment {

    private PlayerMock player;

    @BeforeEach
    void setUp() {
        loadManagersAndListeners();

        //Create random players
        player = createRandomOnlinePlayer();
    }

    @Nested
    class AsyncPlayerPreLogin {
        private AsyncPlayerPreLoginEvent event;

        @BeforeEach
        void setUp() {
            event = EventUtil.newAsyncPlayerPreLoginEvent(player);
        }

        @Test
        @DisplayName("Test when player is still connected but the player attempt to login")
        void testPreLoginDoubleLogin() {
            //player.isConnected is not implemented, hence why we can't do testing
        }

        @Test
        @DisplayName("Test create IUser player profile")
        void testPreLoginCreateUser() {
            assertNull(userManager.getUsers().get(player.getUniqueId()));

            userListener.onPreLoginCreateUser(event);

            IUser user = userManager.getUserNow(player.getUniqueId());

            assertNotNull(user);
            assertEquals(user.getRealName(), event.getName());
            //assertEquals(user.getFirstSeen(), System.currentTimeMillis());
            //assertEquals(user.getLastSeen(), System.currentTimeMillis());
            assertEquals(user.getLastServer(), "testEnvironment");
            assertEquals(user.getIP(), event.getAddress().getHostAddress());
            for (UserSettings settings : UserSettings.values()) {
                assertNotNull(user.getSettings().get(settings));
            }

            Packet packet = api.getSentPackets().poll();
            assertNotNull(packet);
            verify(api.getJedis()).publish(IPacketHandler.CHANNEL, PacketUtil.encode(packet));
        }
    }

}

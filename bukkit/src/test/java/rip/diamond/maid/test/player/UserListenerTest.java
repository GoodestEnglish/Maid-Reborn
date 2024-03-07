package rip.diamond.maid.test.player;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.UserSettings;
import rip.diamond.maid.disguise.Disguise;
import rip.diamond.maid.event.PlayerDisguiseEvent;
import rip.diamond.maid.redis.messaging.IPacketHandler;
import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.redis.packets.bukkit.ProfileUpdatePacket;
import rip.diamond.maid.redis.packets.bukkit.chat.StaffMessagePacket;
import rip.diamond.maid.user.UserMock;
import rip.diamond.maid.util.*;

import java.util.UUID;

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
            assertEquals(user.getClass(), UserMock.class);
            assertEquals(user.getRealName(), event.getName());
            assertNotEquals(user.getFirstSeen(), 0);
            assertNotEquals(user.getLastSeen(), 0);
            assertEquals(user.getLastServer(), "testEnvironment");
            assertEquals(user.getIP(), event.getAddress().getHostAddress());
            for (UserSettings settings : UserSettings.values()) {
                assertNotNull(user.getSettings().get(settings));
            }

            Packet packet = api.getPacket(ProfileUpdatePacket.class);
            assertNotNull(packet);
            verify(api.getJedis()).publish(IPacketHandler.CHANNEL, PacketUtil.encode(packet));
        }
    }

    @Nested
    class PlayerLogin {
        private PlayerLoginEvent event;
        private IUser user;

        @BeforeEach
        void setUp() {
            event = EventUtil.newPlayerLoginEvent(player);

            //Create a default user profile
            user = new UserMock(player.getUniqueId(), rankManager, punishmentManager);
            userManager.getUsers().put(player.getUniqueId(), user);
        }

        @Test
        @DisplayName("Test player isn't allow to join when the server doesn't allow")
        void testLoginStartupCheck() {
            serverManager.setLoaded(false);

            userListener.onLoginStartupCheck(event);

            assertEquals(event.getResult(), PlayerLoginEvent.Result.KICK_OTHER);
            assertEquals(Common.legacy(event.kickMessage()), Common.legacy(CC.RED + "伺服器暫時不允許玩家進入, 請稍後再試"));
        }
    }

    @Nested
    class PlayerJoin {
        private String playerName;
        private UUID playerUUID;
        private IUser user;
        private PlayerJoinEvent event;

        @BeforeEach
        void setUp() {
            playerName = "UserListenerDummy";
            playerUUID = UUID.fromString("12345678-1234-1234-1234-1234567890ab");
            player = new PlayerMock(server, playerName, playerUUID);

            //Create a default user profile
            user = new UserMock(player.getUniqueId(), rankManager, punishmentManager);
            userManager.getUsers().put(player.getUniqueId(), user);

            event = EventUtil.newPlayerJoinEvent(player);
        }

        @Test
        @DisplayName("Test cache player's data when player join the server")
        void testJoinCache() {
            userListener.onJoinCache(event);

            assertEquals(UUIDCache.getUUID(playerName).join(), playerUUID);
            assertEquals(UUIDCache.getUsername(playerUUID).join(), playerName);
        }

        @Test
        @DisplayName("Test player without a disguise behavior when player join the server")
        void testJoinDisguise1() {
            assertNull(user.getDisguise());

            userListener.onJoinDisguise(event);

            assertNull(player.nextMessage());
            server.getPluginManager().assertEventNotFired(PlayerDisguiseEvent.class);
        }

        @Test
        @DisplayName("Test player with a disguise behavior when player join the server")
        void testJoinDisguise2() {
            user.setDisguise(new Disguise("DisguisedPlayer", "skinName", rankManager.getDefaultRank().getUniqueID()));

            userListener.onJoinDisguise(event);

            assertEquals(player.nextMessage(), Common.legacy(CC.GREEN + "成功偽裝成為: DisguisedPlayer"));
            server.getPluginManager().assertEventFired(PlayerDisguiseEvent.class);
        }
    }
}

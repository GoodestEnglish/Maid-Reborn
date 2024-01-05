package rip.diamond.maid.disguise;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.BiomeManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.extend.MaidManager;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;

//Credit: DisguiseAPI by itspinger (https://github.com/itspinger/DisguiseAPI)
// TODO: 5/1/2024 Skin doesn't show in opponent's view
public class DisguiseManager extends MaidManager {

    public void disguise(Player player, Disguise disguise) {
        setSkin(player, disguise.getSkinValue(), disguise.getSkinSignature());
        setName(player, disguise.getName());
        sendServerPackets(player);
    }

    public void unDisguise(Player player) {
        // TODO: 5/1/2024
    }

    private void setSkin(Player player, String value, String signature) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        Property property = new Property("textures", value, signature);

        // Clear the properties of this player and add the new ones
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", property);
    }

    private void setName(Player player, String name) {
        GameProfile profile = ((CraftPlayer) player).getProfile();

        try {
            Field field = GameProfile.class.getDeclaredField("name");

            // Set the field accessible and update it
            field.setAccessible(true);
            field.set(profile, name);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        player.displayName(Common.text(name));
        player.playerListName(Common.text(name));
    }


    public void sendServerPackets(Player player) {
        // Get the entity player from the base player
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        ServerLevel level = sp.serverLevel();

        // Create the PacketPlayOutRespawn packet
        ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(new CommonPlayerSpawnInfo(
                level.dimensionTypeId(),
                level.dimension(),
                BiomeManager.obfuscateSeed(level.getSeed()),
                sp.gameMode.getGameModeForPlayer(),
                sp.gameMode.getPreviousGameModeForPlayer(),
                level.isDebug(),
                level.isFlat(),
                sp.getLastDeathLocation(),
                sp.getPortalCooldown()
        ), (byte) 3);

        // Get the name and stuff
        Location l = player.getLocation();

        // Send position
        ClientboundPlayerPositionPacket pos = new ClientboundPlayerPositionPacket(
                l.getX(),
                l.getY(),
                l.getZ(),
                l.getYaw(),
                l.getPitch(),
                new HashSet<>(),
                0
        );

        PlayerUpdate update = new PlayerUpdate(player);

        // Remove the player from the list
        sendPacket(player, new ClientboundPlayerInfoRemovePacket(Collections.singletonList(sp.getUUID())));
        sendPacket(player, ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.singletonList(sp)));

        // Send the respawn packet to the player
        // With the respawn packet, the player gets to see their own skin
        sendPacket(player, respawn);
        sendPacket(player, pos);

        //Set the player properties back to before
        update.send();

        // Update scale
        ((CraftPlayer) player).updateScaledHealth();
        sp.onUpdateAbilities();
        sp.resetSentInfo();

        // Send the refresh packet to other players
        // Where they will be able to see the updated skin
        refreshPlayer(player);
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.connection.send(packet);
    }

    private void refreshPlayer(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) {
                continue;
            }
            other.hidePlayer(plugin, player);
            other.showPlayer(plugin, player);
        }
    }

    static class PlayerUpdate {
        private final Player player;
        private final GameMode gameMode;
        private final boolean allowFlight;
        private final boolean flying;
        private final Location location;
        private final int level;
        private final float xp;

        public PlayerUpdate(Player player) {
            this.player = player;

            // Update other fields
            this.gameMode = player.getGameMode();
            this.allowFlight = player.getAllowFlight();
            this.flying = player.isFlying();
            this.location = player.getLocation();
            this.level = player.getLevel();
            this.xp = player.getExp();
        }

        /**
         * This method applies the saved properties to the
         * given player.
         */

        public void send() {
            if (this.player == null) {
                return;
            }

            // Update the properties here
            this.player.setGameMode(this.gameMode);
            this.player.setAllowFlight(this.allowFlight);
            this.player.setFlying(this.flying);
            this.player.teleport(this.location);
            this.player.updateInventory();
            this.player.setLevel(this.level);
            this.player.setExp(this.xp);
        }
    }
}

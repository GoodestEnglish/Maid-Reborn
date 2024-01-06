package rip.diamond.maid.disguise;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IDisguise;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.config.Config;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.BroadcastPacket;
import rip.diamond.maid.util.Alert;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.extend.MaidManager;

import java.lang.reflect.Field;
import java.util.*;

public class DisguiseManager extends MaidManager {

    private final Map<UUID, ProfileProperty> playerProperties = new HashMap<>();
    private final Map<String, ProfileProperty> skinProperties = new HashMap<>();

    public DisguiseManager() {
        List<String> skins = Config.DISGUISE_SKIN.toStringList();
        for (String skin : skins) {
            cacheSkin(skin, false);
        }
    }

    public void disguise(Player player, IDisguise disguise, boolean join) {
        ProfileProperty property = player.getPlayerProfile().getProperties().stream().filter(property_ -> property_.getName().equals("textures")).findAny().orElseThrow(() -> new NoSuchElementException("Cannot find texture property for " + player.getName()));
        playerProperties.put(player.getUniqueId(), property);

        ProfileProperty skinProperty = skinProperties.get(disguise.getSkinName());
        if (skinProperty == null) {
            Common.sendMessage(player, CC.RED + "錯誤: 無法獲取偽裝的皮膚資料, 請重新嘗試進行偽裝");
            return;
        }
        setName(player, disguise.getName());
        setSkin(player, skinProperty);

        //Save the disguise data
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();
        Maid.INSTANCE.getUserManager().saveUser(user);

        //Output message
        Common.sendMessage(player, CC.GREEN + "成功偽裝成為: " + disguise.getName());
        if (!join) {
            String serverID = MaidAPI.INSTANCE.getPlatform().getServerID();
            Alert alert = Alert.DISGUISED;
            PacketHandler.send(new BroadcastPacket(serverID, alert.getType().getPermission(), ImmutableList.of(alert.get(user.getRealName(), serverID, user.getName()))));
        }
    }

    public void unDisguise(Player player) {
        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();
        user.setDisguise(null);

        setName(player, user.getRealName());
        setSkin(player, playerProperties.get(user.getUniqueID()));

        //Save the disguise data
        Maid.INSTANCE.getUserManager().saveUser(user);

        //Output message
        Common.sendMessage(player, CC.GREEN + "成功解除當前的偽裝");
    }

    /**
     * Set the skin of the player.
     * {@link Player#setPlayerProfile(PlayerProfile)} will refresh all player's view, so we don't need to deal with packets.
     */
    private void setSkin(Player player, ProfileProperty property) {
        PlayerProfile profile = player.getPlayerProfile();
        profile.setProperty(property);
        player.setPlayerProfile(profile);
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

    public Map.Entry<String, ProfileProperty> getRandomSkin() {
        if (skinProperties.isEmpty()) {
            return null;
        }
        int index = new Random().nextInt(skinProperties.size());
        Iterator<Map.Entry<String, ProfileProperty>> iterator = skinProperties.entrySet().iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        return iterator.next();
    }

    public void cacheSkin(String username, boolean async) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
        if (async) {
            offlinePlayer.getPlayerProfile().update().thenAcceptAsync(profile -> {
                ProfileProperty property = profile.getProperties().stream().filter(property_ -> property_.getName().equals("textures")).findAny().orElseThrow(() -> new NoSuchElementException("Cannot find texture property for " + offlinePlayer.getName()));
                skinProperties.put(profile.getName(), property);

                Common.log(CC.GREEN + "已成功加載 " + profile.getName() + " 的皮膚");
            });
        } else {
            PlayerProfile profile = offlinePlayer.getPlayerProfile();
            profile.complete(true);
            ProfileProperty property = profile.getProperties().stream().filter(property_ -> property_.getName().equals("textures")).findAny().orElseThrow(() -> new NoSuchElementException("Cannot find texture property for " + offlinePlayer.getName()));
            skinProperties.put(profile.getName(), property);

            Common.log(CC.GREEN + "已成功加載 " + profile.getName() + " 的皮膚");
        }
    }
}

package rip.diamond.maid.disguise;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IDisguise;
import rip.diamond.maid.api.user.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DisguisePacketAdapter extends PacketAdapter {

    private final Maid plugin;

    public DisguisePacketAdapter(Maid plugin, ListenerPriority listenerPriority) {
        super(plugin, listenerPriority, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.PLAYER_INFO);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();

        if (event.getPacketType() == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
            UUID uuid = packet.getUUIDs().read(0);
            Player target = Bukkit.getPlayer(uuid);
            if (target == null) {
                return;
            }

            IUser user = plugin.getUserManager().getUser(uuid).join();
            IDisguise disguise = user.getDisguise();
            if (disguise != null) {
                packet.getUUIDs().write(0, disguise.getUniqueID());
                event.setPacket(packet);
            }
        }
        //DONT use this, because other place like tab complete will use the real name anyways
        /* else if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
            if (packet.getPlayerInfoDataLists().size() > 0) {
                List<PlayerInfoData> newData = new ArrayList<>();

                for (PlayerInfoData playerInfoData : packet.getPlayerInfoDataLists().read(1)) {
                    WrappedGameProfile oldProfile = playerInfoData.getProfile();
                    if (oldProfile == null) {
                        continue;
                    }
                    UUID uuid = oldProfile.getUUID();
                    if (uuid == null) {
                        continue;
                    }
                    IUser user = plugin.getUserManager().getUser(uuid).join();
                    IDisguise disguise = user.getDisguise();
                    if (disguise == null) {
                        continue;
                    }

                    WrappedGameProfile newProfile = new WrappedGameProfile(disguise.getUniqueID(), disguise.getName());
                    newProfile.getProperties().put("textures", new WrappedSignedProperty("textures", disguise.getSkinValue(), disguise.getSkinSignature()));
                    //newData.add(new PlayerInfoData(newProfile, playerInfoData.getLatency(), playerInfoData.getGameMode(), playerInfoData.getDisplayName()));
                    newData.add(new PlayerInfoData(newProfile.getUUID(), playerInfoData.getLatency(), true, playerInfoData.getGameMode(), newProfile, playerInfoData.getDisplayName(), playerInfoData.getRemoteChatSessionData()));

                    packet.getPlayerInfoDataLists().write(1, newData);
                }
            }
        }*/
    }
}

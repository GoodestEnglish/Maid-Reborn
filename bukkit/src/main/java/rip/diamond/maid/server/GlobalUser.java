package rip.diamond.maid.server;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.server.IGlobalUser;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.UserSettings;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class GlobalUser implements IGlobalUser {

    private UUID uniqueID;
    private String name, simpleDisplayName, displayName, texture, currentServer;
    private Map<UserSettings, String> settings;
    private long lastTick;

    public static GlobalUser of(IUser user) {
        GlobalUser globalUser = new GlobalUser();
        globalUser.setUniqueID(user.getUniqueID());
        globalUser.setName(user.getName());
        globalUser.setSimpleDisplayName(user.getSimpleDisplayName(false));
        globalUser.setDisplayName(user.getDisplayName(false));
        globalUser.setTexture(user.getTexture());
        globalUser.setCurrentServer(MaidAPI.INSTANCE.getPlatform().getServerID());
        globalUser.setSettings(ImmutableMap.copyOf(user.getSettings()));
        globalUser.setLastTick(System.currentTimeMillis());
        return globalUser;
    }

}

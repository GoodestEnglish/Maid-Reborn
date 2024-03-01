package rip.diamond.maid.server;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
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
@Builder
public class GlobalUser implements IGlobalUser {

    private UUID uniqueID;
    private String name, simpleDisplayName, displayName, texture, currentServer;
    private Map<UserSettings, String> settings;
    private long lastTick;

    public static GlobalUser of(IUser user) {
        return GlobalUser.builder()
                .uniqueID(user.getUniqueID())
                .name(user.getName())
                .simpleDisplayName(user.getSimpleDisplayName(false))
                .displayName(user.getDisplayName(false))
                .texture(user.getTexture())
                .currentServer(MaidAPI.INSTANCE.getPlatform().getServerID())
                .settings(ImmutableMap.copyOf(user.getSettings()))
                .lastTick(System.currentTimeMillis())
                .build();
    }

}

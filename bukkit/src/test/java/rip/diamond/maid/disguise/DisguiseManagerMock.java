package rip.diamond.maid.disguise;

import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.entity.Player;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.config.DisguiseConfig;
import rip.diamond.maid.player.UserManager;

public class DisguiseManagerMock extends DisguiseManager {
    public DisguiseManagerMock(IMaidAPI api, UserManager userManager, DisguiseConfig config) {
        super(api, userManager, config);

        skinProperties.put("skinName", new ProfileProperty("", ""));
    }

    @Override
    public void cacheSkin(String username, boolean async) {
        //PlayerProfile.complete isn't implemented in MockBukkit, so we empty this function
    }

    @Override
    public void setName(Player player, String name) {
        //This method involves NMS, MockBukkit doesn't support NMS, so we empty this function
    }
}

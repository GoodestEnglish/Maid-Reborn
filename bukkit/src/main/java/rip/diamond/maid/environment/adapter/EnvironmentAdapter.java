package rip.diamond.maid.environment.adapter;

import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.environment.IEnvironment;
import rip.diamond.maid.player.User;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.RankManager;

import java.util.UUID;

public class EnvironmentAdapter implements IEnvironment {
    @Override
    public IUser createUser(UUID uniqueID, RankManager rankManager, PunishmentManager punishmentManager) {
        return new User(uniqueID);
    }
}

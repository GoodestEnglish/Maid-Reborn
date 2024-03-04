package rip.diamond.maid.environment;

import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.RankManager;
import rip.diamond.maid.user.UserMock;

import java.util.UUID;

public class EnvironmentMock implements IEnvironment {
    @Override
    public IUser createUser(UUID uniqueID, RankManager rankManager, PunishmentManager punishmentManager) {
        return new UserMock(uniqueID, rankManager, punishmentManager);
    }
}

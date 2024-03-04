package rip.diamond.maid.environment;

import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.RankManager;

import java.util.UUID;

public interface IEnvironment {
    
    IUser createUser(UUID uniqueID, RankManager rankManager, PunishmentManager punishmentManager);
    
}

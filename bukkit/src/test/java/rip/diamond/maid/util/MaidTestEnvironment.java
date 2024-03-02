package rip.diamond.maid.util;

import rip.diamond.maid.chat.ChatListener;
import rip.diamond.maid.chat.ChatManager;
import rip.diamond.maid.config.ChatConfigMock;
import rip.diamond.maid.config.MongoConfigMock;
import rip.diamond.maid.mongo.MongoManager;
import rip.diamond.maid.player.UserManager;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.RankManager;

public class MaidTestEnvironment extends ServerTestEnvironment {

    protected UserManager userManager;
    protected MongoManager mongoManager;
    protected RankManager rankManager;
    protected ChatManager chatManager;
    protected PunishmentManager punishmentManager;

    protected ChatListener chatListener;

    public void loadManagersAndListeners() {
        userManager = new UserManager();
        mongoManager = new MongoManager(new MongoConfigMock());
        rankManager = new RankManager(mongoManager);
        chatManager = new ChatManager(new ChatConfigMock());
        punishmentManager = new PunishmentManager(mongoManager, userManager);

        chatListener = new ChatListener(chatManager, userManager);
    }

}

package rip.diamond.maid.util;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import com.github.fppt.jedismock.RedisServer;
import rip.diamond.maid.MaidAPIMock;
import rip.diamond.maid.chat.ChatListener;
import rip.diamond.maid.chat.ChatManager;
import rip.diamond.maid.config.ChatConfig;
import rip.diamond.maid.config.ChatConfigMock;
import rip.diamond.maid.config.MongoConfigMock;
import rip.diamond.maid.mongo.MongoManager;
import rip.diamond.maid.player.UserManager;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.RankManager;

public class MaidTestEnvironment extends ServerTestEnvironment {

    protected MockPlugin plugin;
    protected MaidAPIMock api;

    protected ChatConfig chatConfig;

    protected UserManager userManager;
    protected MongoManager mongoManager;
    protected RankManager rankManager;
    protected ChatManager chatManager;
    protected PunishmentManager punishmentManager;

    protected ChatListener chatListener;

    public void loadManagersAndListeners() {
        plugin = MockBukkit.createMockPlugin("MaidMock");
        api = new MaidAPIMock(server);

        chatConfig = new ChatConfigMock();

        userManager = new UserManager(api);
        mongoManager = new MongoManager(new MongoConfigMock());
        rankManager = new RankManager(api, mongoManager);
        chatManager = new ChatManager(api, userManager, chatConfig);
        punishmentManager = new PunishmentManager(api, mongoManager, userManager);

        chatListener = new ChatListener(plugin, api, chatManager, userManager);
    }

}

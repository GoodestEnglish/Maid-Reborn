package rip.diamond.maid.util;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
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
import rip.diamond.maid.util.task.ITaskRunner;
import rip.diamond.maid.util.task.TaskRunnerMock;

public class MaidTestEnvironment extends ServerTestEnvironment {

    protected MockPlugin plugin;
    protected MaidAPIMock api;
    protected ITaskRunner task;

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
        task = new TaskRunnerMock(server, plugin);

        chatConfig = new ChatConfigMock();

        mongoManager = new MongoManager(new MongoConfigMock());
        userManager = new UserManager(api, task, mongoManager);
        rankManager = new RankManager(api, mongoManager);
        chatManager = new ChatManager(api, userManager, chatConfig);
        punishmentManager = new PunishmentManager(api, task, mongoManager, userManager);

        chatListener = new ChatListener(plugin, api, chatManager, userManager);
    }

}

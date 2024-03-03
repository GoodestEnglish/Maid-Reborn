package rip.diamond.maid.util;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import rip.diamond.maid.MaidAPIMock;
import rip.diamond.maid.chat.ChatListener;
import rip.diamond.maid.chat.ChatManager;
import rip.diamond.maid.config.*;
import rip.diamond.maid.disguise.DisguiseManager;
import rip.diamond.maid.mongo.MongoManager;
import rip.diamond.maid.player.UserManager;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.RankManager;
import rip.diamond.maid.server.ServerManager;
import rip.diamond.maid.util.task.ITaskRunner;
import rip.diamond.maid.util.task.TaskRunnerMock;

public class MaidTestEnvironment extends ServerTestEnvironment {

    protected MockPlugin plugin;
    protected MaidAPIMock api;
    protected ITaskRunner task;

    protected ChatConfig chatConfig;
    protected DisguiseConfig disguiseConfig;
    protected MongoConfig mongoConfig;
    protected ServerConfig serverConfig;

    protected MongoManager mongoManager;
    protected UserManager userManager;
    protected RankManager rankManager;
    protected ServerManager serverManager;
    protected ChatManager chatManager;
    protected PunishmentManager punishmentManager;

    protected ChatListener chatListener;

    public void loadManagersAndListeners() {
        plugin = MockBukkit.createMockPlugin("MaidMock");
        api = new MaidAPIMock(server);
        task = new TaskRunnerMock(server, plugin);

        chatConfig = new ChatConfigMock();
        disguiseConfig = new DisguiseConfigMock();
        mongoConfig = new MongoConfigMock();
        serverConfig = new ServerConfigMock(api);

        mongoManager = new MongoManager(mongoConfig);
        userManager = new UserManager(api, task, mongoManager);
        rankManager = new RankManager(api, mongoManager);
        serverManager = new ServerManager(serverConfig);
        chatManager = new ChatManager(api, userManager, chatConfig);
        punishmentManager = new PunishmentManager(api, task, mongoManager, userManager);

        chatListener = new ChatListener(plugin, api, chatManager, userManager);
    }

}

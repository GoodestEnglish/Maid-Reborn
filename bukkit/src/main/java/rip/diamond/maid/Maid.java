package rip.diamond.maid;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;
import rip.diamond.maid.config.adapter.ConfigAdapter;
import rip.diamond.maid.chat.ChatListener;
import rip.diamond.maid.chat.ChatManager;
import rip.diamond.maid.command.*;
import rip.diamond.maid.config.Config;
import rip.diamond.maid.disguise.DisguiseManager;
import rip.diamond.maid.mongo.MongoManager;
import rip.diamond.maid.nametag.NameTagManager;
import rip.diamond.maid.permission.PermissionManager;
import rip.diamond.maid.platform.BukkitPlatform;
import rip.diamond.maid.player.UserListener;
import rip.diamond.maid.player.UserManager;
import rip.diamond.maid.punishment.PunishmentListener;
import rip.diamond.maid.punishment.PunishmentManager;
import rip.diamond.maid.rank.Rank;
import rip.diamond.maid.rank.RankManager;
import rip.diamond.maid.redis.RedisCredentials;
import rip.diamond.maid.server.ServerListener;
import rip.diamond.maid.server.ServerManager;
import rip.diamond.maid.util.BasicConfigFile;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.CommandService;
import rip.diamond.maid.util.command.Drink;
import rip.diamond.maid.util.command.provider.custom.RankProvider;
import rip.diamond.maid.util.menu.MenuHandler;
import rip.diamond.maid.util.task.ITaskRunner;
import rip.diamond.maid.util.task.TaskRunnerAdapter;

import java.text.DecimalFormat;
import java.util.Arrays;

@Getter
public class Maid extends JavaPlugin {

    public static Maid INSTANCE;
    public static ITaskRunner TASK;
    public static MaidAPI API;
    public static DecimalFormat FORMAT = new DecimalFormat("#0.00");

    private CommandService drink;
    private MongoManager mongoManager;
    private UserManager userManager;
    private RankManager rankManager;
    private ServerManager serverManager;
    private ChatManager chatManager;
    private PermissionManager permissionManager;
    private DisguiseManager disguiseManager;
    private PunishmentManager punishmentManager;
    private NameTagManager nameTagManager;

    private BasicConfigFile configFile;
    private ConfigAdapter configAdapter;

    @Override
    public void onEnable() {
        INSTANCE = this;

        loadFile();
        loadAPI();
        loadManagers();
        loadListeners();
        loadCommands();

        //Log information
        Arrays.asList(
                "<aqua>==================================================",
                "<aqua>Maid <white>已成功啟動",
                "",
                "<white>名稱: <aqua>" + getDescription().getName(),
                "<white>版本: <aqua>" + getDescription().getVersion(),
                "<white>作者: <aqua>" + StringUtils.join(getDescription().getAuthors(), ", "),
                "<aqua>=================================================="
        ).forEach(Common::log);
    }

    @Override
    public void onDisable() {
        API.stop();
    }

    private void loadFile() {
        this.configFile = new BasicConfigFile(this, "config.yml");
        this.configAdapter = new ConfigAdapter();

        Config.loadDefault();
    }

    private void loadAPI() {
        //Register command library
        drink = Drink.get(this);
        //Register MenuAPI instance
        new MenuHandler(this);
        //Register TaskRunner instance
        TASK = new TaskRunnerAdapter();
        //Register API instance
        API = new MaidAPI(new BukkitPlatform(TASK));
        API.start(new RedisCredentials(Config.REDIS_HOST.toString(), Config.REDIS_PORT.toInteger(), Config.REDIS_AUTH.toBoolean(), Config.REDIS_PASSWORD.toString()));
    }

    private void loadManagers() {
        mongoManager = new MongoManager(configAdapter);
        userManager = new UserManager(API, TASK, mongoManager);
        rankManager = new RankManager(API, mongoManager);
        serverManager = new ServerManager(configAdapter);
        chatManager = new ChatManager(API, userManager, configAdapter);
        permissionManager = new PermissionManager();
        disguiseManager = new DisguiseManager(API, userManager, configAdapter);
        punishmentManager = new PunishmentManager(API, TASK, mongoManager, userManager);
        nameTagManager = new NameTagManager();
    }

    private void loadListeners() {
        Arrays.asList(
                new ChatListener(this, API, chatManager, userManager),
                new UserListener(TASK, userManager, mongoManager, serverManager),
                new PunishmentListener(API, userManager, punishmentManager),
                new ServerListener(API)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void loadCommands() {
        drink.bind(Rank.class).toProvider(new RankProvider());

        drink.register(new AltsCommand(), "alts");
        drink.register(new BanCommand(), "ban");
        drink.register(new ChatCommand(API), "chat");
        drink.register(new ColorCommand(), "color");
        drink.register(new DisguiseCommand(), "disguise", "nick");
        drink.register(new GrantCommand(), "grant");
        drink.register(new GrantsCommand(), "grants");
        drink.register(new IPBanCommand(), "ipban");
        drink.register(new KickCommand(), "kick");
        drink.register(new ListCommand(), "list");
        drink.register(new MessageCommand(), "message", "msg", "tell", "dm", "m");
        drink.register(new MuteCommand(), "mute");
        drink.register(new PacketTestCommand(API), "packettest");
        drink.register(new PlayersCommand(), "players");
        drink.register(new PermissionCommand(), "permission", "perms");
        drink.register(new PunishmentCommand(), "punishment");
        drink.register(new RankCommand(), "rank");
        drink.register(new ReplyCommand(), "reply", "r");
        drink.register(new ServersCommand(), "servers");
        drink.register(new SettingsCommand(), "settings", "setting", "option");
        drink.register(new StaffChatCommand(API), "staffchat", "sc");
        drink.register(new TestCommand(), "test");
        drink.register(new UnbanCommand(), "unban");
        drink.register(new UndisguiseCommand(), "undisguise", "unnick");
        drink.register(new UnmuteCommand(), "unmute");
        drink.register(new UserCommand(), "user");
        drink.register(new WarnCommand(), "warn");
        drink.registerCommands();
    }


}

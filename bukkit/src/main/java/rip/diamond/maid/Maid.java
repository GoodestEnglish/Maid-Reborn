package rip.diamond.maid;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;
import rip.diamond.maid.chat.ChatListener;
import rip.diamond.maid.chat.ChatManager;
import rip.diamond.maid.command.*;
import rip.diamond.maid.config.Config;
import rip.diamond.maid.disguise.DisguiseManager;
import rip.diamond.maid.mongo.MongoManager;
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

import java.util.Arrays;

@Getter
public class Maid extends JavaPlugin {

    public static Maid INSTANCE;

    private CommandService drink;
    private MongoManager mongoManager;
    private UserManager userManager;
    private RankManager rankManager;
    private ServerManager serverManager;
    private ChatManager chatManager;
    private PermissionManager permissionManager;
    private DisguiseManager disguiseManager;
    private PunishmentManager punishmentManager;

    private BasicConfigFile configFile;

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
        MaidAPI.INSTANCE.stop();
    }

    private void loadFile() {
        this.configFile = new BasicConfigFile(this, "config.yml");

        Config.loadDefault();
    }

    private void loadAPI() {
        drink = Drink.get(this);
        new MenuHandler(this); //Register MenuAPI instance
        new MaidAPI(new RedisCredentials(Config.REDIS_HOST.toString(), Config.REDIS_PORT.toInteger(), Config.REDIS_AUTH.toBoolean(), Config.REDIS_PASSWORD.toString()), new BukkitPlatform());
    }

    private void loadManagers() {
        mongoManager = new MongoManager();
        userManager = new UserManager();
        rankManager = new RankManager();
        serverManager = new ServerManager();
        chatManager = new ChatManager();
        permissionManager = new PermissionManager();
        disguiseManager = new DisguiseManager();
        punishmentManager = new PunishmentManager();
    }

    private void loadListeners() {
        Arrays.asList(
                new ChatListener(),
                new UserListener(),
                new PunishmentListener(),
                new ServerListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void loadCommands() {
        drink.bind(Rank.class).toProvider(new RankProvider());

        drink.register(new BanCommand(), "ban");
        drink.register(new ColorCommand(), "color");
        drink.register(new DisguiseCommand(), "disguise", "nick");
        drink.register(new GrantCommand(), "grant");
        drink.register(new GrantsCommand(), "grants");
        drink.register(new PacketTestCommand(), "packettest");
        drink.register(new PermissionCommand(), "permission", "perms");
        drink.register(new PunishmentCommand(), "punishment");
        drink.register(new RankCommand(), "rank");
        drink.register(new TestCommand(), "test");
        drink.register(new UnbanCommand(), "unban");
        drink.register(new UndisguiseCommand(), "undisguise", "unnick");
        drink.register(new UserCommand(), "user");
        drink.registerCommands();
    }
}

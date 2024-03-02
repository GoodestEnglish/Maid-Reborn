package rip.diamond.maid.config;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import rip.diamond.maid.Maid;
import rip.diamond.maid.util.BasicConfigFile;
import rip.diamond.maid.util.Common;

import java.util.List;

@AllArgsConstructor
public enum Config {
    SERVER_ID("server-id", "unset"),
    /* Redis */
    REDIS_HOST("redis.host", "localhost"),
    REDIS_PORT("redis.port", 6379),
    REDIS_AUTH("redis.auth", false),
    REDIS_PASSWORD("redis.password", "bar"),
    /* Mongo */
    MONGO_CONNECTION_STRING("mongo.connection-string", "mongodb://127.0.0.1:27017/MaidReborn"),
    MONGO_DATABASE("mongo.database", "Maid"),
    /* Disguises */
    DISGUISE_SKIN("disguise.skin", ImmutableList.of("XiaoNiu_TW", "BedlessNoob", "YuseiFudo", "DULINTW", "Fauzh", "LU__LU", "DragonL")),
    /* Chat */
    CHAT_MUTED("chat.muted", false),
    CHAT_DELAY("chat.delay", 0),
    ;

    @Getter private final String path;
    @Getter private final Object defaultValue;

    public String toString() {
        String str = Maid.INSTANCE.getConfigFile().getString(path);
        if (str.equals(path)) {
            return defaultValue.toString();
        }
        return str;
    }


    public List<String> toStringList() {
        List<String> str = Maid.INSTANCE.getConfigFile().getStringList(path);
        if (str.isEmpty() || str.get(0).equals(path)) {
            return (List<String>) defaultValue;
        }
        if (str.get(0).equals("null")) {
            return ImmutableList.of();
        }
        return str;
    }

    public boolean toBoolean() {
        return Boolean.parseBoolean(toString());
    }

    public int toInteger() {
        return Integer.parseInt(toString());
    }

    public double toDouble() {
        return Double.parseDouble(toString());
    }

    public void setValue(Object object) {
        BasicConfigFile configFile = Maid.INSTANCE.getConfigFile();

        configFile.getConfiguration().set(path, object);

        configFile.save();
        configFile.load();
    }

    public static void loadDefault() {
        BasicConfigFile configFile = Maid.INSTANCE.getConfigFile();

        for (Config config : Config.values()) {
            String path = config.getPath();
            String str = configFile.getString(path);
            if (str.equals(path)) {
                Common.debug("沒有找到 '" + path + "'... 正在加入到 config.yml");
                configFile.getConfiguration().set(path, config.getDefaultValue());
            }
        }

        configFile.save();
        configFile.load();

        if (SERVER_ID.toString().equals(SERVER_ID.getDefaultValue()) && !Maid.MOCKING) {
            Common.log("請在 config.yml 更改 " + SERVER_ID.getPath());
            Bukkit.shutdown();
        }
    }

}

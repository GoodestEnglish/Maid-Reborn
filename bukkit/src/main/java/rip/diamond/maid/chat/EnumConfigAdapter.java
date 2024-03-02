package rip.diamond.maid.chat;

import rip.diamond.maid.config.Config;

public class EnumConfigAdapter implements ChatConfig, MongoConfig {

    @Override
    public int getChatDelay() {
        return Config.CHAT_DELAY.toInteger();
    }

    @Override
    public void setChatDelay(int delay) {
        Config.CHAT_DELAY.setValue(delay);
    }

    @Override
    public boolean isChatMuted() {
        return Config.CHAT_MUTED.toBoolean();
    }

    @Override
    public void setChatMuted(boolean muted) {
        Config.CHAT_MUTED.setValue(muted);
    }

    @Override
    public String getConnectionString() {
        return Config.MONGO_CONNECTION_STRING.toString();
    }

    @Override
    public String getDatabase() {
        return Config.MONGO_DATABASE.toString();
    }

    @Override
    public void setConnectionString(String connectionString) {
        Config.MONGO_CONNECTION_STRING.setValue(connectionString);
    }

    @Override
    public void setDatabase(String database) {
        Config.MONGO_DATABASE.setValue(database);
    }
}

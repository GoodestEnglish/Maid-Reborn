package rip.diamond.maid.chat;

public interface MongoConfig {
    String getConnectionString();

    String getDatabase();

    void setConnectionString(String connectionString);

    void setDatabase(String database);
}

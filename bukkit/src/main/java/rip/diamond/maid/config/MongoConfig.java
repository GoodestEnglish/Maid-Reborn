package rip.diamond.maid.config;

public interface MongoConfig {
    String getConnectionString();

    String getDatabase();

    void setConnectionString(String connectionString);

    void setDatabase(String database);
}
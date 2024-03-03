package rip.diamond.maid.config;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class MongoConfigMock implements MongoConfig {

    private final MongoServer server;

    public MongoConfigMock() {
        this.server = new MongoServer(new MemoryBackend());
        this.server.bind();
    }

    @Override
    public String getConnectionString() {
        return server.getConnectionString();
    }

    @Override
    public String getDatabase() {
        return "database";
    }

    @Override
    public void setConnectionString(String connectionString) {

    }

    @Override
    public void setDatabase(String database) {

    }
}

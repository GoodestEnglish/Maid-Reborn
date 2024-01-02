package rip.diamond.maid.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import rip.diamond.maid.config.Config;
import rip.diamond.maid.util.extend.MaidManager;

public class MongoManager extends MaidManager {

    private final MongoDatabase database;
    private final MongoClient client;
    @Getter private final MongoCollection<Document> users, ranks, punishments;

    public MongoManager() {
        this.client = MongoClients.create(Config.MONGO_CONNECTION_STRING.toString());
        this.database = client.getDatabase(Config.MONGO_DATABASE.toString());

        this.users = this.database.getCollection("users");
        this.ranks = this.database.getCollection("ranks");
        this.punishments = this.database.getCollection("punishments");
    }

}

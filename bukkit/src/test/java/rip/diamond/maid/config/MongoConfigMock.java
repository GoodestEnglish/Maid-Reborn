package rip.diamond.maid.config;

public class MongoConfigMock implements MongoConfig {
    @Override
    public String getConnectionString() {
        return "mongodb://uccszwhp6s9lj9pudki0:aEmPZdaHfq0StkLgLDRQ@n1-c2-mongodb-clevercloud-customers.services.clever-cloud.com:27017,n2-c2-mongodb-clevercloud-customers.services.clever-cloud.com:27017/btqygcxraiqx7yn?replicaSet=rs0";
    }

    @Override
    public String getDatabase() {
        return "btqygcxraiqx7yn";
    }

    @Override
    public void setConnectionString(String connectionString) {

    }

    @Override
    public void setDatabase(String database) {

    }
}

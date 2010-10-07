package org.axonframework.samples.trader.app.query;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Helper class to interact with the Mongodb instance.</p>
 * <p>For one database is used. Methods are available to obtain a connection to this database. Other methods are
 * available to obtain a reference to collections immediately for data insertion and querying</p>
 *
 * @author Jettro Coenradie
 */
@Component
public class MongoHelper {
    private Mongo mongoDb;

    public DBCollection users() {
        return getDatabase().getCollection("users");
    }

    public DBCollection tradeItems() {
        return getDatabase().getCollection("tradeitems");
    }

    public DBCollection tradesExecuted() {
        return getDatabase().getCollection("tradesexecuted");
    }

    public DBCollection orderBooks() {
        return getDatabase().getCollection("orderbooks");
    }

    public DBCollection orders() {
        return getDatabase().getCollection("orders");
    }

    public DB getDatabase() {
        return mongoDb.getDB("axontrader");
    }

    @Autowired
    public void setMongoDb(Mongo mongoDb) {
        this.mongoDb = mongoDb;
    }
}

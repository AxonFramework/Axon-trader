package org.axonframework.samples.trader.app.eventstore.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

/**
 * <p>Helper class for interacting with the MongoDB instance. You can use the helper to get access to the mongodb instance
 * and obtain references to the collection required by axon.</p>
 *
 * @author Jettro Coenradie
 */
public class MongoHelper {
    private static final String DEFAULT_DOMAINEVENTS_COLLECTION = "domainevents";
    private static final String DEFAULT_SNAPSHOTEVENTS_COLLECTION = "snapshotevents";
    private static final String DEFAULT_AXONFRAMEWORK_DATABASE = "axonframework";

    private Mongo mongoDb;
    private String databaseName = DEFAULT_AXONFRAMEWORK_DATABASE;
    private String domainEventsCollectionName = DEFAULT_DOMAINEVENTS_COLLECTION;
    private String snapshotEventsCollectionName = DEFAULT_SNAPSHOTEVENTS_COLLECTION;

    public MongoHelper(Mongo mongoDb) {
        this.mongoDb = mongoDb;
    }

    public DBCollection domainEvents() {
        return database().getCollection(domainEventsCollectionName);
    }

    public DBCollection snapshotEvents() {
        return database().getCollection(snapshotEventsCollectionName);
    }

    public DB database() {
        return mongoDb.getDB(databaseName);
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setDomainEventsCollectionName(String domainEventsCollectionName) {
        this.domainEventsCollectionName = domainEventsCollectionName;
    }

    public void setSnapshotEventsCollectionName(String snapshotEventsCollectionName) {
        this.snapshotEventsCollectionName = snapshotEventsCollectionName;
    }
}

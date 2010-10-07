package org.axonframework.samples.trader.app.eventstore.mongo;

import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jettro Coenradie
 */
public class MongoInitOnce implements InitializingBean {
    private MongoHelper systemMongo;
    private EventBus eventBus;

    @Autowired
    public MongoInitOnce(MongoHelper systemMongo, EventBus eventBus) {
        this.systemMongo = systemMongo;
        this.eventBus = eventBus;
    }

    public void createItems() {
        systemMongo.database().dropDatabase();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        createItems();
    }
}

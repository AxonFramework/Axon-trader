package org.axonframework.samples.trader.app.eventstore.mongo;

import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Factory bean for a Mongo instance class. This factory is required since we want to support the most basic setup as
 * well as a more advanced setup. The most basic setup makes use of only one instance of Mongo. This scenario is not
 * suitable for a production environment, but it does work for a test environment.</p>
 * <p>The factory supports two environments:</p>
 * <ul>
 * <li>Test - configure this factory with setTestContext(true)</li>
 * <li>Production - the list of provide <code>ServerAddress</code> instances becomes mandatory.</li>
 * </ul>
 * <p>For production usage we expect at least 1 server to be configured. If not, an <code>IllegalStateException</code>
 * is thrown. Be warned, it is better to provide multiple servers in case of a Replica Set.</p>
 *
 * @author Jettro Coenradie
 */
public class MongoFactory implements FactoryBean<Mongo>, InitializingBean, DisposableBean {
    private boolean testContext;
    private List<ServerAddress> mongoAddresses;
    private Mongo mongo;

    public MongoFactory() {
        testContext = false;
        mongoAddresses = new ArrayList<ServerAddress>();
    }

    @Override
    public Mongo getObject() throws Exception {
        return this.mongo;
    }

    @Override
    public Class<?> getObjectType() {
        return Mongo.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * Sets the testContext, provide true if you want the test context and false if you want the production context
     *
     * @param testContext Boolean indicating the context, true for test and false for production.
     */
    public void setTestContext(String testContext) {
        this.testContext = Boolean.parseBoolean(testContext);
    }

    public void setMongoAddresses(List<ServerAddress> mongoAddresses) {
        this.mongoAddresses = mongoAddresses;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (testContext) {
            this.mongo = new Mongo();
        } else {
            if (mongoAddresses.isEmpty()) {
                throw new IllegalStateException("Please configure at least 1 instance of Mongo for production.");
            }
            this.mongo = new Mongo(mongoAddresses);
        }
    }

    @Override
    public void destroy() throws Exception {
        this.mongo.close();
    }
}

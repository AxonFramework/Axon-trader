package org.axonframework.samples.trader.app.eventstore.mongo;

import org.axonframework.domain.*;
import org.axonframework.eventstore.EventStore;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jettro Coenradie
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/persistence-infrastructure-context.xml",
        "classpath:META-INF/spring/configuration-context.xml", "classpath:META-INF/spring/command-context.xml",
        "classpath:META-INF/spring/cqrs-infrastructure-context.xml"})
public class EventStoreBenchmark {
    private static final int THREAD_COUNT = 100;
    private static final int TRANSACTION_COUNT = 500;
    private static final int TRANSACTION_SIZE = 2;

    @Autowired
    private MongoEventStore mongoEventStore;

    @Autowired
    private MongoHelper mongoHelper;

    // FileSystem: 100 threads concurrently wrote 100 * 10 events each in 29625 milliseconds. That is an average of 3448 events per second
    // FileSystem: 100 threads concurrently wrote 500 * 2 events each in 44813 milliseconds. That is an average of 2272 events per second
    // FileSystem (OutputStream pooling): 100 threads concurrently wrote 100 * 10 events each in 19844 milliseconds. That is an average of 5263 events per second
    // FileSystem (OutputStream pooling): 100 threads concurrently wrote 500 * 2 events each in 20047 milliseconds. That is an average of 5000 events per second

    @BeforeClass
    public static void checkProductionMongoFactory() {
        System.setProperty("test.context", "false");
    }

    @Before
    public void initMongo() {
        mongoHelper.database().dropDatabase();
    }

    @Test
    public void startBenchmarkTest_Mongo() throws InterruptedException {
        System.setProperty("test.context", "true");

        long start = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<Thread>();
        for (int t = 0; t < THREAD_COUNT; t++) {
            Thread thread = new Thread(new Benchmark());
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format(
                "Filesystem: %s threads concurrently wrote %s * %s events each in %s milliseconds. That is an average of %s events per second",
                THREAD_COUNT,
                TRANSACTION_COUNT,
                TRANSACTION_SIZE,
                (end - start),
                (THREAD_COUNT * TRANSACTION_COUNT * TRANSACTION_SIZE) / ((end - start) / 1000)));
    }

    private int saveAndLoadLargeNumberOfEvents(AggregateIdentifier aggregateId, EventStore eventStore,
                                               int eventSequence) {
        List<DomainEvent> events = new ArrayList<DomainEvent>();
        for (int t = 0; t < TRANSACTION_SIZE; t++) {
            events.add(new StubDomainEvent(aggregateId, eventSequence++));
        }
        eventStore.appendEvents("benchmark", new SimpleDomainEventStream(events));
        return eventSequence;
    }

    private class Benchmark implements Runnable {

        @Override
        public void run() {
            final AggregateIdentifier aggregateId = AggregateIdentifierFactory.randomIdentifier();
            final AtomicInteger eventSequence = new AtomicInteger(0);
            for (int t = 0; t < TRANSACTION_COUNT; t++) {
                eventSequence.set(saveAndLoadLargeNumberOfEvents(aggregateId,
                        mongoEventStore,
                        eventSequence.get()) + 1);
            }
        }
    }
}
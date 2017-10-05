package org.axonframework.samples.trader.query.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("mongodb")
@EnableMongoRepositories(
        basePackages = "org.axonframework.samples.trader.query.*.repositories",
        mongoTemplateRef = "mongoSpringTemplate")
public class MongoDbConfiguration {
}

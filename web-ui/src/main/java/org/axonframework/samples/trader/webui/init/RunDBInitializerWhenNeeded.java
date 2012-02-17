/*
 * Copyright (c) 2010-2012. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.webui.init;

import org.axonframework.samples.trader.query.users.UserEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * <p>Special class used to initialize the database when starting the container. The database is only initialized
 * when the collection "UserEntry" is not yet available.</p>
 * <p>We need to check for the display name of the application context since we by default have two using spring-mvc
 * the way we do.</p>
 *
 * @author Jettro Coenradie
 */
@Component
public class RunDBInitializerWhenNeeded implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RunDBInitializerWhenNeeded.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        DBInit init = event.getApplicationContext().getBean(DBInit.class);
        MongoTemplate mongoTemplate = event.getApplicationContext().getBean(MongoTemplate.class);

        if ("Root WebApplicationContext".equals(event.getApplicationContext().getDisplayName())) {
            if (!mongoTemplate.collectionExists(UserEntry.class)) {
                init.createItems();
                logger.info("The database has been created and refreshed with some data.");
            }
        }
    }
}

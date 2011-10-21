/*
 * Copyright (c) 2010. Gridshore
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

package org.axonframework.samples.trader.app.query.company;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.samples.trader.app.api.company.CompanyCreatedEvent;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jettro Coenradie
 */
@Component
public class CompanyListener {
    private MongoHelper mongo;


    @EventHandler
    public void handleCompanyCreatedEvent(CompanyCreatedEvent event) {
        DBObject companyEntry = BasicDBObjectBuilder.start()
                .add("identifier", event.getCompanyIdentifier().asString())
                .add("name", event.getCompanyName())
                .add("value", event.getCompanyValue())
                .add("amountOfShares", event.getAmountOfShares())
                .add("tradeStarted", true)
                .get();
        mongo.companies().insert(companyEntry);
    }

    @Autowired
    public void setMongoHelper(MongoHelper mongoHelper) {
        this.mongo = mongoHelper;
    }
}

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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.axonframework.samples.trader.app.query.MongoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jettro Coenradie
 */
@Component
public class CompanyRepositoryMongo implements CompanyRepository {
    private MongoHelper mongo;

    @Override
    public List<CompanyEntry> listAllCompanies() {
        DBCursor companiesCursor = mongo.companies().find();
        List<CompanyEntry> companies = new ArrayList<CompanyEntry>();
        while (companiesCursor.hasNext()) {
            DBObject nextCompany = companiesCursor.next();
            CompanyEntry companyEntry = mapCompanyFromMongo(nextCompany);
            companies.add(companyEntry);
        }
        return companies;
    }

    @Override
    public CompanyEntry findCompanyByIdentifier(String companyIdentifier) {
        DBObject query = BasicDBObjectBuilder.start().add("identifier", companyIdentifier).get();
        return mapCompanyFromMongo(mongo.companies().findOne(query));
    }

    @Override
    public CompanyEntry findCompanyByOrderBookIdentifier(String orderBookIdentifier) {
        DBObject query = BasicDBObjectBuilder.start().add("orderBookIdentifier", orderBookIdentifier).get();
        return mapCompanyFromMongo(mongo.companies().findOne(query));
    }

    /**
     * Used to create a CompanyEntry object based on a mongo DBObject
     *
     * @param mongoCompanyObject The mongo object to create a CompanyEntry
     * @return CompanyEntry created from te mongo object
     */
    private CompanyEntry mapCompanyFromMongo(DBObject mongoCompanyObject) {
        CompanyEntry companyEntry = new CompanyEntry();
        companyEntry.setIdentifier((String) mongoCompanyObject.get("identifier"));
        companyEntry.setName((String) mongoCompanyObject.get("name"));
        companyEntry.setValue((Long) mongoCompanyObject.get("value"));
        companyEntry.setAmountOfShares((Long) mongoCompanyObject.get("amountOfShares"));
        companyEntry.setTradeStarted((Boolean) mongoCompanyObject.get("tradeStarted"));
        if (mongoCompanyObject.containsField("orderBookIdentifier")) {
            String orderBookIdentifier = (String) mongoCompanyObject.get("orderBookIdentifier");
            companyEntry.setOrderBookIdentifier(orderBookIdentifier);
        }
        return companyEntry;
    }

    @Autowired
    public void setMongo(MongoHelper mongo) {
        this.mongo = mongo;
    }
}

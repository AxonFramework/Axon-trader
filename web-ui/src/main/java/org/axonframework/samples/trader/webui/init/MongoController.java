/*
 * Copyright (c) 2011. Gridshore
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

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping("/data")
public class MongoController {
    private DBInit dbInit;
    private org.springframework.data.mongodb.core.MongoTemplate springTemplate;

    @Autowired
    public MongoController(DBInit dbInit, MongoTemplate springTemplate) {
        this.dbInit = dbInit;
        this.springTemplate = springTemplate;
    }

    @RequestMapping(value = "/collections", method = RequestMethod.GET)
    public String collections(Model model) {
        Set<String> collectionNames = springTemplate.getCollectionNames();
        model.addAttribute("collections", collectionNames);
        return "data/collections";
    }

    @RequestMapping(value = "/collection/{id}", method = RequestMethod.GET)
    public String collection(@PathVariable("id") String collectionName,
                             @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                             @RequestParam(value = "itemsperpage", defaultValue = "5") int itemsPerPage,
                             Model model) {
        DBCursor dbCursor = springTemplate.getCollection(collectionName).find();
        List<DBObject> dbObjects = dbCursor.skip((pageNumber - 1) * itemsPerPage).limit(itemsPerPage).toArray();

        List<Map> items = new ArrayList<Map>(dbCursor.length());
        for (DBObject dbObject : dbObjects) {
            items.add(dbObject.toMap());
        }
        model.addAttribute("items", items);

        int totalItems = dbCursor.count();
        int numPages = ((int) Math.floor(totalItems / itemsPerPage)) + 1;
        model.addAttribute("numPages", numPages);
        model.addAttribute("page", pageNumber);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("collectionName", collectionName);
        return "data/collection";
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String initializeMongo(Model model) {
        dbInit.createItems();

        model.addAttribute("info", "Mongo database is initialized.");
        return "data/info";
    }

    @RequestMapping(value = "/portfolio/money/{identifier}/{amount}")
    public String addMoneyToPortfolio(@PathVariable("identifier") String portfolioIdentifier,
                                      @PathVariable("amount") long amount,
                                      Model model) {
        dbInit.depositMoneyToPortfolio(portfolioIdentifier, amount);
        model.addAttribute("info", "Added money to the portfolio.");
        return "data/info";
    }
}

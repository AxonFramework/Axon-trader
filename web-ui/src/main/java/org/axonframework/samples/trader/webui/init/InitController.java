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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

/**
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping("/init")
public class InitController {
    private DBInit dbInit;
    private org.springframework.data.mongodb.core.MongoTemplate springTemplate;
    private MongoDbFactory factory;

    @Autowired
    public InitController(DBInit dbInit, MongoTemplate springTemplate, MongoDbFactory factory) {
        this.dbInit = dbInit;
        this.springTemplate = springTemplate;
        this.factory = factory;
    }

    @RequestMapping(value = "/mongo", method = RequestMethod.GET)
    public String initializeMongo(Model model) {
        dbInit.createItems();
        Set<String> collectionNames = springTemplate.getCollectionNames();
        StringBuilder sb = new StringBuilder();
        for (String name : collectionNames) {
            sb.append(name);
            sb.append("  ");
        }

        model.addAttribute("info", sb.toString());
        return "init/mongo";
    }
}

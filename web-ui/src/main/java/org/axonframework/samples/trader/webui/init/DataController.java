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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/data")
public class DataController {

    private DBInit dbInit;

    @Autowired
    public DataController(DBInit dbInit) {
        this.dbInit = dbInit;
    }

    @RequestMapping(value = "/collections", method = RequestMethod.GET)
    public String collections(Model model) {
        model.addAttribute("collections", dbInit.obtainCollectionNames());
        return "data/collections";
    }

    @RequestMapping(value = "/collection/{id}", method = RequestMethod.GET)
    public String collection(@PathVariable("id") String collectionName,
                             @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                             @RequestParam(value = "itemsperpage", defaultValue = "5") int itemsPerPage,
                             Model model) {
        DataResults dataResults = dbInit.obtainCollection(collectionName,
                                                          itemsPerPage,
                                                          (pageNumber - 1) * itemsPerPage + 1);

        model.addAttribute("items", dataResults.getItems());

        int totalItems = dataResults.getTotalItems();
        int numPages = ((int) Math.floor(totalItems / itemsPerPage)) + 1;
        model.addAttribute("numPages", numPages);
        model.addAttribute("page", pageNumber);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("collectionName", collectionName);
        return "data/collection";
    }

    @RequestMapping(value = "/portfolio/money/{identifier}/{amount}")
    public String addMoneyToPortfolio(@PathVariable("identifier") String portfolioIdentifier,
                                      @PathVariable("amount") long amount,
                                      Model model) {
        dbInit.depositMoneyToPortfolio(portfolioIdentifier, amount);
        model.addAttribute("info", "Added cash to the portfolio.");
        return "data/info";
    }
}

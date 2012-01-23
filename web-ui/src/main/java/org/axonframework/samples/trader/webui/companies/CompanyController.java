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

package org.axonframework.samples.trader.webui.companies;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.domain.StringAggregateIdentifier;
import org.axonframework.samples.trader.app.api.transaction.StartBuyTransactionCommand;
import org.axonframework.samples.trader.app.api.transaction.StartSellTransactionCommand;
import org.axonframework.samples.trader.app.query.company.CompanyEntry;
import org.axonframework.samples.trader.app.query.company.repositories.CompanyQueryRepository;
import org.axonframework.samples.trader.app.query.orderbook.OrderBookEntry;
import org.axonframework.samples.trader.app.query.orderbook.repositories.OrderBookQueryRepository;
import org.axonframework.samples.trader.app.query.portfolio.PortfolioEntry;
import org.axonframework.samples.trader.app.query.portfolio.repositories.PortfolioQueryRepository;
import org.axonframework.samples.trader.app.query.tradeexecuted.TradeExecutedEntry;
import org.axonframework.samples.trader.app.query.tradeexecuted.repositories.TradeExecutedQueryRepository;
import org.axonframework.samples.trader.app.query.user.UserEntry;
import org.axonframework.samples.trader.app.query.user.repositories.UserQueryRepository;
import org.axonframework.samples.trader.webui.order.AbstractOrder;
import org.axonframework.samples.trader.webui.order.BuyOrder;
import org.axonframework.samples.trader.webui.order.SellOrder;
import org.axonframework.samples.trader.webui.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping("/company")
public class CompanyController {

    private CompanyQueryRepository companyRepository;
    private OrderBookQueryRepository orderBookRepository;
    private UserQueryRepository userRepository;
    private TradeExecutedQueryRepository tradeExecutedRepository;
    private PortfolioQueryRepository portfolioQueryRepository;
    private CommandBus commandBus;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public CompanyController(CompanyQueryRepository companyRepository,
                             CommandBus commandBus,
                             UserQueryRepository userRepository,
                             OrderBookQueryRepository orderBookRepository,
                             TradeExecutedQueryRepository tradeExecutedRepository,
                             PortfolioQueryRepository portfolioQueryRepository) {
        this.companyRepository = companyRepository;
        this.commandBus = commandBus;
        this.userRepository = userRepository;
        this.orderBookRepository = orderBookRepository;
        this.tradeExecutedRepository = tradeExecutedRepository;
        this.portfolioQueryRepository = portfolioQueryRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("items", companyRepository.findAll());
        return "company/list";
    }

    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET)
    public String details(@PathVariable String identifier, Model model) {
        CompanyEntry company = companyRepository.findOne(identifier);
        OrderBookEntry bookEntry = orderBookRepository.findByCompanyIdentifier(company.getIdentifier()).get(0);
        List<TradeExecutedEntry> executedTrades = tradeExecutedRepository.findByOrderBookIdentifier(bookEntry.getIdentifier());
        model.addAttribute("company", company);
        model.addAttribute("sellOrders", bookEntry.sellOrders());
        model.addAttribute("buyOrders", bookEntry.buyOrders());
        model.addAttribute("executedTrades", executedTrades);
        return "company/details";
    }


    @RequestMapping(value = "/buy/{identifier}", method = RequestMethod.GET)
    public String buyForm(@PathVariable String identifier, Model model) {
        BuyOrder order = new BuyOrder();
        prepareInitialOrder(identifier, order);
        model.addAttribute("order", order);
        return "company/buy";
    }

    @RequestMapping(value = "/sell/{identifier}", method = RequestMethod.GET)
    public String sellForm(@PathVariable String identifier, Model model) {
        SellOrder order = new SellOrder();
        prepareInitialOrder(identifier, order);
        model.addAttribute("order", order);
        return "company/sell";
    }

    @RequestMapping(value = "/sell/{identifier}", method = RequestMethod.POST)
    public String sell(@ModelAttribute("order") @Valid SellOrder order, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            UserEntry username = userRepository.findByUsername(SecurityUtil.obtainLoggedinUsername());

            // TODO: make this work for multiple orderbooks per company
            OrderBookEntry bookEntry = orderBookRepository.findByCompanyIdentifier(order.getCompanyId()).get(0);
            PortfolioEntry portfolioEntry = portfolioQueryRepository.findByUserIdentifier(username.getIdentifier());
            StartSellTransactionCommand command = new StartSellTransactionCommand(
                    new StringAggregateIdentifier(bookEntry.getIdentifier()),
                    new StringAggregateIdentifier(portfolioEntry.getIdentifier()),
                    order.getTradeCount(),
                    order.getItemPrice());

            commandBus.dispatch(command);

            return "redirect:/company/" + order.getCompanyId();
        }
        return "company/sell";
    }

    @RequestMapping(value = "/buy/{identifier}", method = RequestMethod.POST)
    public String buy(@ModelAttribute("order") @Valid BuyOrder order, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            UserEntry username = userRepository.findByUsername(SecurityUtil.obtainLoggedinUsername());

            // TODO: make this work for multiple orderbooks per company
            OrderBookEntry bookEntry = orderBookRepository.findByCompanyIdentifier(order.getCompanyId()).get(0);
            PortfolioEntry portfolioEntry = portfolioQueryRepository.findByUserIdentifier(username.getIdentifier());
            StartBuyTransactionCommand command = new StartBuyTransactionCommand(
                    new StringAggregateIdentifier(bookEntry.getIdentifier()),
                    new StringAggregateIdentifier(portfolioEntry.getIdentifier()),
                    order.getTradeCount(),
                    order.getItemPrice());
            commandBus.dispatch(command);
            return "redirect:/company/" + order.getCompanyId();
        }

        return "company/buy";
    }

    private void prepareInitialOrder(String identifier, AbstractOrder order) {
        CompanyEntry company = companyRepository.findOne(identifier);
        order.setCompanyId(identifier);
        order.setCompanyName(company.getName());
    }

}

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

package org.axonframework.samples.trader.webui.tradeitem;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.NoOpCallback;
import org.axonframework.domain.StringAggregateIdentifier;
import org.axonframework.samples.trader.app.api.order.CreateBuyOrderCommand;
import org.axonframework.samples.trader.app.api.order.CreateSellOrderCommand;
import org.axonframework.samples.trader.app.query.orderbook.OrderBookEntry;
import org.axonframework.samples.trader.app.query.orderbook.OrderBookRepository;
import org.axonframework.samples.trader.app.query.tradeexecuted.TradeExecutedEntry;
import org.axonframework.samples.trader.app.query.tradeexecuted.TradeExecutedRepository;
import org.axonframework.samples.trader.app.query.tradeitem.TradeItemEntry;
import org.axonframework.samples.trader.app.query.tradeitem.TradeItemRepository;
import org.axonframework.samples.trader.app.query.user.UserEntry;
import org.axonframework.samples.trader.app.query.user.UserRepository;
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
@RequestMapping("/tradeitem")
public class TradeItemController {

    private TradeItemRepository tradeItemRepository;
    private OrderBookRepository orderBookRepository;
    private UserRepository userRepository;
    private TradeExecutedRepository tradeExecutedRepository;
    private CommandBus commandBus;

    @Autowired
    public TradeItemController(TradeItemRepository tradeItemRepository,
                               CommandBus commandBus,
                               UserRepository userRepository,
                               OrderBookRepository orderBookRepository,
                               TradeExecutedRepository tradeExecutedRepository) {
        this.tradeItemRepository = tradeItemRepository;
        this.commandBus = commandBus;
        this.userRepository = userRepository;
        this.orderBookRepository = orderBookRepository;
        this.tradeExecutedRepository = tradeExecutedRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("items", tradeItemRepository.listAllTradeItems());
        return "tradeitem/list";
    }

    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET)
    public String details(@PathVariable String identifier, Model model) {
        TradeItemEntry tradeItem = tradeItemRepository.findTradeItemByIdentifier(identifier);
        OrderBookEntry bookEntry = orderBookRepository.findByTradeItem(tradeItem.getIdentifier());
        List<TradeExecutedEntry> executedTrades = tradeExecutedRepository.findExecutedTradesForOrderBook(bookEntry.getIdentifier());
        model.addAttribute("tradeItem", tradeItem);
        model.addAttribute("sellOrders", bookEntry.sellOrders());
        model.addAttribute("buyOrders", bookEntry.buyOrders());
        model.addAttribute("executedTrades", executedTrades);
        return "tradeitem/details";
    }


    @RequestMapping(value = "/buy/{identifier}", method = RequestMethod.GET)
    public String buyForm(@PathVariable String identifier, Model model) {
        BuyOrder order = new BuyOrder();
        prepareInitialOrder(identifier, order);
        model.addAttribute("order", order);
        return "tradeitem/buy";
    }

    @RequestMapping(value = "/sell/{identifier}", method = RequestMethod.GET)
    public String sellForm(@PathVariable String identifier, Model model) {
        SellOrder order = new SellOrder();
        prepareInitialOrder(identifier, order);
        model.addAttribute("order", order);
        return "tradeitem/sell";
    }

    @RequestMapping(value = "/sell/{identifier}", method = RequestMethod.POST)
    public String sell(@ModelAttribute("order") @Valid SellOrder order, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            UserEntry username = userRepository.findByUsername(SecurityUtil.obtainLoggedinUsername());

            String tradeItemId = order.getTradeItemId();
            TradeItemEntry tradeItemByIdentifier = tradeItemRepository.findTradeItemByIdentifier(tradeItemId);

            CreateSellOrderCommand command = new CreateSellOrderCommand(
                    new StringAggregateIdentifier(username.getIdentifier()),
                    new StringAggregateIdentifier(tradeItemByIdentifier.getOrderBookIdentifier()),
                    order.getTradeCount(),
                    order.getItemPrice());

            commandBus.dispatch(command, NoOpCallback.INSTANCE);

            return "redirect:/tradeitem/" + order.getTradeItemId();
        }
        return "tradeitem/sell";
    }

    @RequestMapping(value = "/buy/{identifier}", method = RequestMethod.POST)
    public String buy(@ModelAttribute("order") BuyOrder order, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            UserEntry username = userRepository.findByUsername(SecurityUtil.obtainLoggedinUsername());

            String tradeItemId = order.getTradeItemId();
            TradeItemEntry tradeItemByIdentifier = tradeItemRepository.findTradeItemByIdentifier(tradeItemId);

            CreateBuyOrderCommand command = new CreateBuyOrderCommand(
                    new StringAggregateIdentifier(username.getIdentifier()),
                    new StringAggregateIdentifier(tradeItemByIdentifier.getOrderBookIdentifier()),
                    order.getTradeCount(),
                    order.getItemPrice());
            commandBus.dispatch(command, NoOpCallback.INSTANCE);
            return "redirect:/tradeitem/" + order.getTradeItemId();
        }

        return "tradeitem/buy";
    }

    private void prepareInitialOrder(String identifier, AbstractOrder order) {
        TradeItemEntry tradeItem = tradeItemRepository.findTradeItemByIdentifier(identifier);
        order.setTradeItemId(identifier);
        order.setTradeItemName(tradeItem.getName());
    }

}

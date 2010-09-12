package org.axonframework.samples.trader.webui.order;

import org.axonframework.samples.trader.app.query.orderbook.OrderBookEntry;
import org.axonframework.samples.trader.app.query.orderbook.OrderBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping("/orderbook")
public class OrderBookController {
    private OrderBookRepository repository;

    @Autowired
    public OrderBookController(OrderBookRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("items", repository.listAllOrderBooks());
        return "orderbook/list";
    }

    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET)
    public String getOrders(@PathVariable String identifier, Model model) {
        OrderBookEntry orderBook = repository.findByIdentifier(UUID.fromString(identifier));
        model.addAttribute("orderBook", orderBook);
        return "orderbook/orders";
    }

}

package org.axonframework.samples.trader.webui.order;

import org.axonframework.samples.trader.app.query.OrderBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String buy(@PathVariable String identifier, Model model) {
//        repository.
        return "orderbook/orders";
    }

}

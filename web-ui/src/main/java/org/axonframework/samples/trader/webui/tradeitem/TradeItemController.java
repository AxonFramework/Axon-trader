package org.axonframework.samples.trader.webui.tradeitem;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.samples.trader.app.api.CreateSellOrderCommand;
import org.axonframework.samples.trader.webui.order.SellOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping("/tradeitem")
public class TradeItemController {

    private TradeItemRepository tradeItemRepository;
    private CommandBus commandBus;

    @Autowired
    public TradeItemController(TradeItemRepository tradeItemRepository, CommandBus commandBus) {
        this.tradeItemRepository = tradeItemRepository;
        this.commandBus = commandBus;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("items", tradeItemRepository.list());
        return "tradeitem/list";
    }

    @RequestMapping(value = "/buy/{identifier}", method = RequestMethod.GET)
    public String buy(@PathVariable String identifier, Model model) {
        model.addAttribute("identifier",identifier);
        return "tradeitem/buy";
    }

    @RequestMapping(value = "/sell/{identifier}", method = RequestMethod.GET)
    public String sellForm(@PathVariable String identifier, Model model) {
        model.addAttribute("identifier",identifier);
        model.addAttribute("order", new SellOrder());
        return "tradeitem/sell";
    }

    @RequestMapping(value = "/sell/{identifier}", method = RequestMethod.POST)
    public String sell(@ModelAttribute("order") SellOrder order, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            CreateSellOrderCommand command = new CreateSellOrderCommand(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    order.getTradeCount(),
                    order.getItemPrice());
            FutureCallback callback = new FutureCallback();
            commandBus.dispatch(command,callback);
            try {
                Object o = callback.get();
                System.out.println("toString " + o.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return "tradeitem/sell";
    }

}

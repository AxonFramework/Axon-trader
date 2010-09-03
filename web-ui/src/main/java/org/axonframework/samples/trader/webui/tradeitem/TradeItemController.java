package org.axonframework.samples.trader.webui.tradeitem;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.NoOpCallback;
import org.axonframework.samples.trader.app.api.CreateBuyOrderCommand;
import org.axonframework.samples.trader.app.api.CreateSellOrderCommand;
import org.axonframework.samples.trader.app.query.TradeItemEntry;
import org.axonframework.samples.trader.app.query.TradeItemRepository;
import org.axonframework.samples.trader.app.query.user.UserEntry;
import org.axonframework.samples.trader.app.query.user.UserRepository;
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

import java.util.UUID;

/**
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping("/tradeitem")
public class TradeItemController {

    private TradeItemRepository tradeItemRepository;
    private UserRepository userRepository;
    private CommandBus commandBus;

    @Autowired
    public TradeItemController(TradeItemRepository tradeItemRepository, CommandBus commandBus, UserRepository userRepository) {
        this.tradeItemRepository = tradeItemRepository;
        this.commandBus = commandBus;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("items", tradeItemRepository.listAllTradeItems());
        return "tradeitem/list";
    }

    @RequestMapping(value = "/buy/{identifier}", method = RequestMethod.GET)
    public String buyForm(@PathVariable String identifier, Model model) {
        SellOrder order = new SellOrder();
        order.setTradeItemId(identifier);
        model.addAttribute("order", order);
        return "tradeitem/buy";
    }

    @RequestMapping(value = "/sell/{identifier}", method = RequestMethod.GET)
    public String sellForm(@PathVariable String identifier, Model model) {
        SellOrder order = new SellOrder();
        order.setTradeItemId(identifier);
        model.addAttribute("order", order);
        return "tradeitem/sell";
    }

    @RequestMapping(value = "/sell/{identifier}", method = RequestMethod.POST)
    public String sell(@ModelAttribute("order") SellOrder order, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            UserEntry username = userRepository.findByUsername(SecurityUtil.obtainLoggedinUsername());

            UUID tradeItemId = UUID.fromString(order.getTradeItemId());
            TradeItemEntry tradeItemByIdentifier = tradeItemRepository.findTradeItemByIdentifier(tradeItemId);

            CreateSellOrderCommand command = new CreateSellOrderCommand(
                    username.getIdentifier(),
                    tradeItemByIdentifier.getOrderBookIdentifier(),
                    order.getTradeCount(),
                    order.getItemPrice());

            commandBus.dispatch(command, NoOpCallback.INSTANCE);

        }
        return "tradeitem/sell";
    }

    @RequestMapping(value = "/buy/{identifier}", method = RequestMethod.POST)
    public String buy(@ModelAttribute("order") SellOrder order, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            UserEntry username = userRepository.findByUsername(SecurityUtil.obtainLoggedinUsername());

            UUID tradeItemId = UUID.fromString(order.getTradeItemId());
            TradeItemEntry tradeItemByIdentifier = tradeItemRepository.findTradeItemByIdentifier(tradeItemId);

            CreateBuyOrderCommand command = new CreateBuyOrderCommand(
                    username.getIdentifier(),
                    tradeItemByIdentifier.getOrderBookIdentifier(),
                    order.getTradeCount(),
                    order.getItemPrice());
            commandBus.dispatch(command, NoOpCallback.INSTANCE);
        }
        return "tradeitem/sell";
    }

}

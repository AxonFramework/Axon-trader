package org.axonframework.samples.trader.webui.tradeitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping("/tradeitem")
public class TradeItemController {

    private TradeItemRepository tradeItemRepository;

    @Autowired
    public TradeItemController(TradeItemRepository tradeItemRepository) {
        this.tradeItemRepository = tradeItemRepository;
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
    public String sell(@PathVariable String identifier, Model model) {
        model.addAttribute("identifier",identifier);
        return "tradeitem/sell";
    }

}

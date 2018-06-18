package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.samples.trader.api.company.CompanyId;
import org.axonframework.samples.trader.api.company.CreateCompanyCommand;
import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.samples.trader.api.portfolio.cash.DepositCashCommand;
import org.axonframework.samples.trader.api.portfolio.stock.AddItemsToPortfolioCommand;
import org.axonframework.samples.trader.api.users.CreateUserCommand;
import org.axonframework.samples.trader.api.users.UserId;
import org.axonframework.samples.trader.query.company.CompanyView;
import org.axonframework.samples.trader.query.company.repositories.CompanyViewRepository;
import org.axonframework.samples.trader.query.orderbook.OrderBookView;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookViewRepository;
import org.axonframework.samples.trader.query.portfolio.PortfolioView;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioViewRepository;

import java.util.List;

/**
 * Base class for all DBInit implementations
 */
public abstract class BaseDBInit implements DBInit {

    private CommandBus commandBus;
    private CompanyViewRepository companyRepository;
    private PortfolioViewRepository portfolioRepository;
    private OrderBookViewRepository orderBookRepository;

    protected BaseDBInit(CommandBus commandBus, CompanyViewRepository companyRepository,
                         PortfolioViewRepository portfolioRepository, OrderBookViewRepository orderBookRepository) {
        this.commandBus = commandBus;
        this.companyRepository = companyRepository;
        this.portfolioRepository = portfolioRepository;
        this.orderBookRepository = orderBookRepository;
    }

    @Override
    public void createItems() {
        initializeDB();

        UserId buyer1 = createuser("Buyer One", "buyer1");
        UserId buyer2 = createuser("Buyer two", "buyer2");
        UserId buyer3 = createuser("Buyer three", "buyer3");
        UserId buyer4 = createuser("Buyer four", "buyer4");
        UserId buyer5 = createuser("Buyer five", "buyer5");
        UserId buyer6 = createuser("Buyer six", "buyer6");

        createCompanies(buyer1);

        addMoney(buyer1, 100000);
        addItems(buyer2, "Philips", 10000l);
        addMoney(buyer3, 100000);
        addItems(buyer4, "Shell", 10000l);
        addMoney(buyer5, 100000);
        addItems(buyer6, "Bp", 10000l);

        additionalDBSteps();
    }

    public void depositMoneyToPortfolio(String portfolioIdentifier, long amountOfMoney) {
        DepositCashCommand command =
                new DepositCashCommand(new PortfolioId(portfolioIdentifier), amountOfMoney);
        commandBus.dispatch(new GenericCommandMessage<>(command));
    }

    UserId createuser(String longName, String userName) {
        UserId userId = new UserId();
        CreateUserCommand createUser = new CreateUserCommand(userId, longName, userName, userName);
        commandBus.dispatch(new GenericCommandMessage<>(createUser));
        return userId;
    }

    void createCompanies(UserId userIdentifier) {
        CreateCompanyCommand command = new CreateCompanyCommand(new CompanyId(),
                                                                userIdentifier,
                                                                "Philips",
                                                                1000,
                                                                10000);
        commandBus.dispatch(new GenericCommandMessage<>(command));

        command = new CreateCompanyCommand(new CompanyId(), userIdentifier, "Shell", 500, 5000);
        commandBus.dispatch(new GenericCommandMessage<>(command));

        command = new CreateCompanyCommand(new CompanyId(), userIdentifier, "Bp", 15000, 100000);
        commandBus.dispatch(new GenericCommandMessage<>(command));
    }

    void addMoney(UserId buyer1, long amount) {
        PortfolioView portfolioView = portfolioRepository.findByUserIdentifier(buyer1.toString());
        depositMoneyToPortfolio(portfolioView.getIdentifier(), amount);
    }

    void addItems(UserId user, String companyName, long amount) {
        PortfolioView portfolioView = portfolioRepository.findByUserIdentifier(user.toString());
        OrderBookView orderBookView = obtainOrderBookByCompanyName(companyName);
        AddItemsToPortfolioCommand command = new AddItemsToPortfolioCommand(
                new PortfolioId(portfolioView.getIdentifier()),
                new OrderBookId(orderBookView.getIdentifier()),
                amount);
        commandBus.dispatch(new GenericCommandMessage<>(command));
    }

    OrderBookView obtainOrderBookByCompanyName(String companyName) {
        Iterable<CompanyView> companyEntries = companyRepository.findAll();
        for (CompanyView entry : companyEntries) {
            if (entry.getName().equals(companyName)) {
                List<OrderBookView> orderBookEntries = orderBookRepository
                        .findByCompanyIdentifier(entry.getIdentifier());

                return orderBookEntries.get(0);
            }
        }
        throw new DBInitException("Problem initializing, could not find company with required name.");
    }

    abstract void initializeDB();

    abstract void additionalDBSteps();
}

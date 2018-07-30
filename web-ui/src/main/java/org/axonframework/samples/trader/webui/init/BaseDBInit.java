package org.axonframework.samples.trader.webui.init;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.samples.trader.api.company.CompanyId;
import org.axonframework.samples.trader.api.orders.OrderBookId;
import org.axonframework.samples.trader.api.portfolio.PortfolioId;
import org.axonframework.samples.trader.api.portfolio.stock.AddItemsToPortfolioCommand;
import org.axonframework.samples.trader.api.users.CreateUserCommand;
import org.axonframework.samples.trader.api.users.UserId;
import org.axonframework.samples.trader.query.company.CompanyView;
import org.axonframework.samples.trader.query.company.repositories.CompanyViewRepository;
import org.axonframework.samples.trader.query.orderbook.OrderBookView;
import org.axonframework.samples.trader.query.orderbook.repositories.OrderBookViewRepository;
import org.axonframework.samples.trader.query.portfolio.PortfolioView;
import org.axonframework.samples.trader.query.portfolio.repositories.PortfolioViewRepository;
import org.axonframework.samples.trader.query.users.UserCommandHandler;
import org.axonframework.samples.trader.query.users.UserView;
import org.axonframework.samples.trader.query.users.repositories.UserViewRepository;

import java.util.List;

/**
 * Base class for all DBInit implementations
 */
public abstract class BaseDBInit implements DBInit {

    private CommandBus commandBus;
    private UserViewRepository userRepository;
    private CompanyViewRepository companyRepository;
    private PortfolioViewRepository portfolioRepository;
    private OrderBookViewRepository orderBookRepository;
    private UserCommandHandler userCommandHandler;

    protected BaseDBInit(CommandBus commandBus, UserViewRepository userRepository, CompanyViewRepository companyRepository,
                         PortfolioViewRepository portfolioRepository, OrderBookViewRepository orderBookRepository,
                         UserCommandHandler userCommandHandler) {
        this.commandBus = commandBus;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.portfolioRepository = portfolioRepository;
        this.orderBookRepository = orderBookRepository;
        this.userCommandHandler = userCommandHandler;
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
//        addItems(buyer1, "Philips", 10000l);
        addMoney(buyer3, 100000);
        //addItems(buyer4, "Shell", 10000l);
        addMoney(buyer5, 100000);
        //addItems(buyer6, "Bp", 10000l);

        additionalDBSteps();
    }

    public void depositMoneyToPortfolio(UserId buyer1, long amountOfMoney) {

        PortfolioView portfolioView = new PortfolioView();
        portfolioView.setIdentifier(new PortfolioId().toString());
        portfolioView.setAmountOfMoney(amountOfMoney);
        portfolioView.setUserIdentifier(buyer1.toString());
        portfolioRepository.save(portfolioView);

        /*DepositCashCommand command =
                new DepositCashCommand(new PortfolioId(portfolioIdentifier), amountOfMoney);
        commandBus.dispatch(new GenericCommandMessage<>(command));*/
    }

    @Override
    public void depositMoneyToPortfolio(String portfolioIdentifier, long amountOfMoney) {

    }

    UserId createuser(String longName, String userName) {
        UserId userId = new UserId();
        UserView userView = new UserView();
        userView.setIdentifier(userId.toString());
        userView.setName(longName);
        userView.setUsername(userName);
        userRepository.save(userView);
        CreateUserCommand createUser = new CreateUserCommand(userId, longName, userName, userName);
        commandBus.subscribe(createUser.getClass().getCanonicalName(), userCommandHandler);
        commandBus.dispatch(new GenericCommandMessage<>(createUser));
        return userId;
    }

    void createCompanies(UserId userIdentifier) {
        //改为repository.save方式初始化数据
        CompanyView companyView = new CompanyView();
        companyView.setIdentifier(new CompanyId().toString());
        companyView.setAmountOfShares(10000);
        companyView.setName("Philips");
        companyView.setValue(1000);
        companyView.setUserIdentifier(userIdentifier.toString());
        companyRepository.save(companyView);

        OrderBookView orderBookView = new OrderBookView();
        orderBookView.setCompanyIdentifier(companyView.getIdentifier());
        orderBookView.setCompanyName(companyView.getName());
        orderBookView.setIdentifier(new OrderBookId().toString());
        orderBookRepository.save(orderBookView);

        /*CreateCompanyCommand command = new CreateCompanyCommand(new CompanyId(),
                userIdentifier,
                "Philips",
                1000,
                10000);

        commandBus.dispatch(new GenericCommandMessage<>(command));

        command = new CreateCompanyCommand(new CompanyId(), userIdentifier, "Shell", 500, 5000);
        commandBus.dispatch(new GenericCommandMessage<>(command));

        command = new CreateCompanyCommand(new CompanyId(), userIdentifier, "Bp", 15000, 100000);
        commandBus.dispatch(new GenericCommandMessage<>(command));*/
    }

    void addMoney(UserId buyer1, long amount) {
        PortfolioView portfolioView = portfolioRepository.findByUserIdentifier(buyer1.toString());
        if (portfolioView == null)
            depositMoneyToPortfolio(buyer1, amount);
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

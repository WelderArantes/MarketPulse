package br.edu.marketpulse.service;

import br.edu.marketpulse.model.*;
import br.edu.marketpulse.repository.*;
import br.edu.marketpulse.legacy.*;
import br.edu.marketpulse.patterns.adapter.*;
import br.edu.marketpulse.patterns.observer.*;

public class TradingApplicationService {
    
    public final InMemoryInvestorRepository investors;
    public final InMemoryOrderRepository orders;

    // Dependências agora são providas de fora (DIP)
    private final QuoteService quotes;
    private final RiskService risk;
    private final NotificationService notifications;
    private final MarketPublisher publisher;

    // Construtor recebe todas as dependências (Injeção de Dependência)
    public TradingApplicationService(
        InMemoryInvestorRepository investors,
        InMemoryOrderRepository orders,
        QuoteService quotes,
        RiskService risk,
        NotificationService notifications,
        MarketPublisher publisher
    ) {
        this.investors = investors;
        this.orders = orders;
        this.quotes = quotes;
        this.risk = risk;
        this.notifications = notifications;
        this.publisher = publisher;
        
        // Mantido aqui temporariamente, mas idealmente seriam injetados também
        this.publisher.subscribe(new EmailObserver());
        this.publisher.subscribe(new AuditObserver());
    }

    public boolean place(Order order) {
        Investor investor = investors.find(order.investorId);
        Quote quote = quotes.get(order.ticker);

        order.status = "VALIDATING";
        orders.save(order);

        // 1. Validação de Risco
        if (!risk.approve(investor, order, quote.price)) {
            order.status = "REJECTED";
            orders.save(order);
            publisher.publish(order, "REJECTED");
            return false;
        }

        // 2. Roteamento (Isolamos a complexidade de acoplamento da corretora)
        boolean accepted = routeToBroker(order);

        // 3. Efetivação e regras de negócio coesas (só desconta SE aceito)
        if (accepted) {
            order.status = "EXECUTED";
            if ("BUY".equals(order.side)) {
                investor.availableCash -= order.quantity * quote.price; 
            }
        } else {
            order.status = "FAILED";
        }
        
        orders.save(order);
        notifications.notify("EMAIL", investor.name, "Ordem " + order.id + " = " + order.status);
        publisher.publish(order, order.status);
        
        return accepted;
    }

    // Extracao de metodo (Extract Method) melhora a coesao e legibilidade.
    // Em uma proxima refatoracao, isso pode virar uma interface BrokerRouter (Strategy).
    private boolean routeToBroker(Order order) {
        if ("ALPHA".equals(order.broker)) {
            return new AlphaBrokerAdapter().submit(order);
        } else {
            return new BetaBrokerApi().execute(order.ticker, "BUY".equals(order.side), order.quantity, order.requestedPrice);
        }
    }

    public Quote quote(String ticker) {
        return quotes.get(ticker);
    } 
    
    public MarketPublisher publisher() {
        return publisher;
    }
}

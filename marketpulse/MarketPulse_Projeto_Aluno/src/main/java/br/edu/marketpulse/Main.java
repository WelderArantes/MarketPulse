package br.edu.marketpulse;

import br.edu.marketpulse.model.*;
import br.edu.marketpulse.patterns.facade.MarketPulseFacade;
import br.edu.marketpulse.patterns.factory.OrderFactory;
// Importações fictícias das abstrações que justificaremos no ADR
import br.edu.marketpulse.repository.InMemoryInvestorRepository;
import br.edu.marketpulse.repository.InMemoryOrderRepository;
import br.edu.marketpulse.service.TradingApplicationService;

public class Main {
    public static void main(String[] args) {
        // 1. Configuração de Dependências (Composition Root)
        // Em vez de esconder os "new" dentro do serviço, nós os declaramos aqui e injetamos
        InMemoryInvestorRepository investorRepo = new InMemoryInvestorRepository();
        InMemoryOrderRepository orderRepo = new InMemoryOrderRepository();
        
        // Injeção de dependência via construtor
        TradingApplicationService tradingService = new TradingApplicationService(investorRepo, orderRepo);
        MarketPulseFacade app = new MarketPulseFacade(tradingService); 

        // 2. Execução do fluxo
        app.registerInvestor(new Investor("I1", "aluno@example.com", "CONSERVATIVE", 50000));
        
        Quote q = app.quote("XPTO3");
        System.out.println("Cotação: " + q.ticker + " = " + q.price);
        
        Order o = OrderFactory.create("LIMIT", "O1", "I1", "XPTO3", "BUY", 10, q.price, "ALPHA");
        System.out.println("Ordem aceita: " + app.place(o) + " status=" + o.status);
    }
}

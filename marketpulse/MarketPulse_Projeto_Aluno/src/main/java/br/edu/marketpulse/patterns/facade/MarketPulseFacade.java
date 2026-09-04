package br.edu.marketpulse.patterns.facade;

import br.edu.marketpulse.model.Investor;
import br.edu.marketpulse.model.Order;
import br.edu.marketpulse.model.Quote;
import br.edu.marketpulse.repository.InMemoryOrderRepository;
import br.edu.marketpulse.service.TradingApplicationService;

public class MarketPulseFacade {
    
    private final TradingApplicationService trading;

    // Construtor atualizado: recebe o serviço injetado pelo Main
    public MarketPulseFacade(TradingApplicationService trading) {
        this.trading = trading;
    }
    
    public void registerInvestor(Investor i) {
        // Acessa o repositório através da variável 'investors' que é pública no serviço
        trading.investors.save(i);
    } 
    
    public boolean place(Order o) {
        return trading.place(o);
    } 
    
    public Quote quote(String ticker) {
        return trading.quote(ticker);
    }
    
    public TradingApplicationService getTradingService() {
        return trading;
    } 
    
    public InMemoryOrderRepository getOrderRepository() {
        return trading.orders;
    }
}

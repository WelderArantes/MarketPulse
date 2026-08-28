# Evidência da entrega

- **Aula:** 01 - Fundamentos e organização do repositório
- **Integrantes:** Gustavo Alves Queiroz
- **Issue:** #1 - Análise de fundamentos e reestruturação inicial (Aula 01)
- **Branch:** `aula-01`
- **PR/merge:** [Inserir Link do Pull Request gerado no GitHub]

## Problema observado

Analisando o código atual, especificamente a classe `TradingApplicationService.java`, observou-se graves problemas de design e violações de princípios de engenharia de software:

1. **Violação do SRP (Princípio da Responsabilidade Única):** A classe age como uma "God Class". Ela cuida da persistência (salvar ordens), da lógica de negócios (descontar saldo do investidor), da validação de risco, do roteamento para diferentes corretoras e do envio de notificações.
2. **Violação do OCP (Princípio do Aberto/Fechado):** O roteamento de ordens utiliza estruturas condicionais rígidas (`if("ALPHA".equals(order.broker)) ... else ...`). Para adicionar uma corretora "GAMA", seria necessário modificar a classe, o que quebra o princípio de que a classe deve estar fechada para modificação e aberta para extensão.
3. **Violação do DIP (Princípio da Inversão de Dependência):** Alto acoplamento devido à instanciação direta de dependências usando a palavra reservada `new` (`new RiskService()`, `new NotificationService()`, `new AlphaBrokerAdapter()`). A classe depende de implementações concretas e não de abstrações (interfaces).
4. **Organização Arquitetural Inadequada:** A estrutura de pastas atual agrupa arquivos por tipo técnico ou puramente didático (ex: `patterns.adapter`, `patterns.observer`, `legacy`), o que dilui a coesão do domínio de negócios.
5. **Anomalia de Transação/Regra de Negócio:** O saldo do investidor é descontado (`investor.availableCash -= ...`) _antes_ da confirmação de execução da ordem externa. Se a operação falhar na corretora, o dinheiro não é estornado no código atual.

## Alteração realizada

Nesta etapa (Aula 01), em conformidade com as instruções, o código-fonte em si não foi modificado. As alterações realizadas foram no âmbito de análise e documentação:

1. **Mapeamento de Conceitos:** Identificação no repositório do que é Requisito, Arquitetura, Design e Implementação.
   - **Requisito:** "O sistema deve consultar cotações e enviar ordens de compra/venda".
   - **Arquitetura:** Uso do padrão de mensageria Publisher/Subscriber (`MarketPublisher`, `EmailObserver`) para desacoplar a notificação de eventos do fluxo principal.
   - **Design:** Uso de Padrões de Projeto localizados, como o _Adapter_ (`AlphaBrokerAdapter`) para padronizar contratos externos, e o _Facade_ (`MarketPulseFacade`) para simplificar o uso do sistema na classe `Main`.
   - **Implementação:** O código Java 17 propriamente dito que orquestra esses componentes (ex: `boolean accepted = new AlphaBrokerAdapter().submit(order);`).

2. **Proposta de Nova Organização de Pastas:**
   Proposta de reestruturação futura baseada em _Separation of Concerns_ (Separação de Preocupações), adotando uma arquitetura em camadas (ou Arquitetura Hexagonal/Clean Architecture simplificada):
   - `br.edu.marketpulse.domain`: Entidades (`Investor`, `Order`), Value Objects e Interfaces de Repositório.
   - `br.edu.marketpulse.application`: Casos de uso e serviços de orquestração.
   - `br.edu.marketpulse.infrastructure`: Implementações de persistência, integrações externas (`BetaBrokerApi`, `AlphaBrokerAdapter`) e serviços de mensageria.
   - `br.edu.marketpulse.main`: Injeção de dependência e ponto de entrada (`Main`).

## Critério de aceitação

- Diferenças entre Requisito, Arquitetura, Design e Implementação registradas e explicadas com exemplos do projeto.
- Nova estrutura de diretórios proposta e justificada tecnicamente (redução de acoplamento e aumento de coesão).
- Violações de design da classe `TradingApplicationService` identificadas e justificadas sob a ótica dos princípios SOLID.

## Evidências de execução/validação

- Documentação `docs/aula01-fundamentos.md` criada.
- A compilação do projeto não foi afetada, garantindo o funcionamento do legado atual (`mvn compile` executado com sucesso).

## Commits principais e autores

- `docs: add aula01-fundamentos.md com analise do TradingApplicationService` - Gustavo Alves Queiroz
- `chore: propor reestruturacao de pacotes baseada em camadas` - Gustavo Alves Queiroz

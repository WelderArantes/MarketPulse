# Evidência da entrega
- **Aula:** 04 - Princípios de design, coesão, acoplamento e SOLID
- **Integrantes:** Gustavo Alves Queiroz
- **Issue:** #4 - Refatoração de acoplamento e injeção de dependência
- **Branch:** `aula-04`
- **PR/merge:** pendente

## Problema observado
A classe `Main` mascarava um alto nível de acoplamento do sistema. O `MarketPulseFacade` e o `TradingApplicationService` estavam instanciando suas próprias dependências internas utilizando a palavra-chave `new` diretamente no código. Isso violava o **DIP (Princípio da Inversão de Dependência)** do SOLID e reduzia drasticamente a coesão, pois as classes de serviço precisavam saber como construir repositórios e regras de risco, dificultando testes unitários e futuras manutenções.

## Alteração realizada
Foi aplicado o padrão **Composition Root** na classe `Main`. O `Main` agora é responsável por instanciar os repositórios e serviços de base e injetá-los (Dependency Injection) nas camadas superiores via construtor. Isso retira a responsabilidade de criação de objetos de dentro da lógica de negócios, aumentando a coesão das classes internas.

## Registro de Decisão Arquitetural (ADR-0003 simplificado)
- **Título:** Adoção de Injeção de Dependência manual no Composition Root
- **Contexto:** Serviços altamente acoplados a implementações concretas (repositórios em memória) dificultam a evolução e testes do legado.
- **Decisão:** Extrair a instanciação de objetos para a classe `Main`, utilizando injeção de dependência via construtor no `MarketPulseFacade` e `TradingApplicationService`.
- **Alternativa descartada:** Utilizar um framework completo de injeção de dependência (como Spring Framework ou Google Guice). Essa opção foi descartada no momento para evitar adicionar complexidade desnecessária de infraestrutura em um sistema que ainda está em fase inicial de refatoração, priorizando o uso de Java puro (Vanilla).

## Comparação Antes / Depois
**Antes:**
- `Main`: Simples, mas escondia problemas estruturais. `new MarketPulseFacade()` engatilhava uma cascata de instâncias rígidas ("hardcoded").
- Acoplamento: Alto. As classes de serviço dependiam de classes concretas.

**Depois:**
- `Main`: Assume seu papel de orquestrador da infraestrutura (Composition Root), tornando as dependências explícitas.
- Acoplamento: Reduzido. O sistema abre caminho para depender de abstrações (Interfaces) no futuro.

## Critério de aceitação
- Acoplamento refatorado sem quebrar o fluxo de execução original.
- Decisão registrada em formato ADR com alternativa descartada explícita.
- Comparação clara entre o estado anterior e o novo design utilizando conceitos de SOLID.

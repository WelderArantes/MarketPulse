```mermaid
flowchart LR

    Main[Main]
    Service[Service]
    Model[Model]
    Repository[Repository]
    Legacy[Legacy]
    Patterns[Patterns]

    Main --> Service
    Main --> Patterns

    Service --> Model
    Service --> Repository
    Service --> Legacy
    Service --> Patterns
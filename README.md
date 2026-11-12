# ProjectSpace

Aplicação Spring Boot com PostgreSQL, pronta para execução via Docker Compose.

## Pré-requisitos

- Docker com o plugin Docker Compose

## Executar a aplicação e o banco

Opcionalmente, copie `.env.example` para `.env` e altere as credenciais. Sem esse
arquivo, o Compose usa valores padrão adequados somente para desenvolvimento.

```bash
cp .env.example .env
docker compose up --build
```

A aplicação ficará disponível em `http://localhost:8080` e o PostgreSQL em
`localhost:5432`.

Para executar em segundo plano:

```bash
docker compose up --build -d
```

Para parar os contêineres sem apagar os dados:

```bash
docker compose down
```

Para parar os contêineres e apagar também o volume do banco:

```bash
docker compose down -v
```

## Executar somente o PostgreSQL

```bash
docker compose up -d postgres
./mvnw spring-boot:run
```

Fora do Docker, a aplicação usa por padrão:

- URL: `jdbc:postgresql://localhost:5432/projectspace`
- usuário: `projectspace`
- senha: `projectspace`

Esses valores podem ser alterados com `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`.
# ProjectSpace
# ProjectSpace

# Registro do processo TDD

## Fase 1 — RED de compilação

- **Status:** concluída
- **Data:** 13/11/2026
- **Hora de conclusão:** 11:54 (BRT/UTC-3)

### O que foi feito

- Criação de 13 arquivos `.feature`, com 44 cenários.
- Criação da suíte `CucumberTest`.
- Criação dos Steps de:
  - criação, edição e visualização de usuário;
  - criação e visualização de projetos;
  - adição, edição, exclusão e visualização de tarefas;
  - adição e visualização de links;
  - criação, vínculo ao projeto e visualização de tags.
- Configuração do Docker para executar o Cucumber com Maven e Java 21.
- Execução dos testes antes da implementação do código de produção.

### Comando executado

```bash
LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
docker compose run --rm --build cucumber
```

### Resultado

O Docker e o Maven funcionaram corretamente. A compilação dos testes terminou com `BUILD FAILURE` porque os DTOs, Entities, Repositories e Services ainda não haviam sido implementados.

Esse resultado registra o primeiro **RED de compilação** do projeto.

### Evidências do RED

- [`results/RED_TDD_FASE1_1.png`](./results/RED_TDD_FASE1_1.png)
- [`results/RED_TDD_FASE1_2.png`](./results/RED_TDD_FASE1_2.png)

## Fase 2 — RED comportamental

- **Status:** concluída
- **Data:** 14/11/2026

### O que foi feito

- Criação dos contratos mínimos de Entities, DTOs, Repositories e Services para:
  - criação, edição e visualização de usuário;
  - criação e visualização de projetos;
  - adição, edição, exclusão e visualização de tarefas;
  - adição e visualização de links;
  - criação, vínculo ao projeto e visualização de tags.
- Execução dos 44 cenários com o código de comportamento ainda vazio.

### Comando executado

```bash
LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
docker compose run --rm --build cucumber
```

### Resultado

O código de produção e os Steps compilaram, e o Cucumber executou todos os cenários:

```text
Tests run: 44, Failures: 44, Errors: 0, Skipped: 0
BUILD FAILURE
```

As 44 falhas são comportamentais: os métodos das Services ainda não persistem, consultam, editam ou excluem dados e ainda não lançam os erros de negócio esperados. Não houve falha de compilação, configuração ou infraestrutura.

Esse resultado registra o **RED comportamental** do projeto.

### Próxima etapa

Implementar nas Services as regras de negócio de:

- criação, edição e visualização de usuário;
- criação e visualização de projetos;
- adição, edição, exclusão e visualização de tarefas;
- adição e visualização de links;
- criação, vínculo ao projeto e visualização de tags.

A fase **GREEN** será registrada quando os 44 cenários passarem.

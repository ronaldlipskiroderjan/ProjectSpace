# Registro do processo TDD

## Etapa 1 — RED

- **Status:** concluída
- **Data:** 13/11/2026
- **Hora de conclusão:** 11:54 (BRT/UTC-3)

### O que foi feito

- Criação de 8 arquivos `.feature`, com 34 cenários.
- Criação da suíte `CucumberTest`.
- Criação dos Steps de usuário, projeto, tarefa, link e tag.
- Configuração do Docker para executar o Cucumber com Maven e Java 21.
- Execução dos testes antes da implementação do código de produção.

### Comando executado

```bash
LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
docker compose run --rm --build cucumber
```

### Resultado

O Docker e o Maven funcionaram corretamente. A compilação dos testes terminou com `BUILD FAILURE` e 57 erros porque os DTOs, Entities, Repositories e Services ainda não foram implementados.

Esse resultado registra o primeiro **RED de compilação** do projeto.

### Evidências do RED

- [`docs/tdd/results/RED_TDD_1.png`](./results/RED_TDD_1.png)
- [`docs/tdd/results/RED_TDD_2.png`](./results/RED_TDD_2.png)

### Próxima etapa

Criar os contratos mínimos do código de produção para os testes compilarem e registrar o **RED comportamental** do Cucumber.

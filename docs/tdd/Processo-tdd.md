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


A fase **GREEN total** será registrada quando os 44 cenários passarem. Cada
incremento funcional concluído será registrado antes disso como um GREEN parcial.

## GREEN parcial: Implementação 1 (usuário)

- **Status:** concluída
- **Data:** 14/11/2026
- **Hora de conclusão:** 23:47 (BRT/UTC-3)

### Escopo implementado

A primeira implementação GREEN concluiu o fluxo de usuário, contemplando:

- cadastro de conta;
- edição da própria conta autenticada;
- visualização do próprio perfil.

### Regras de cadastro

- Validação de nome e e-mail obrigatórios.
- Validação do tamanho mínimo da senha.
- Normalização do e-mail antes da consulta e persistência.
- Rejeição de e-mail duplicado.
- Codificação da senha antes da persistência.
- Associação da role `ROLE_USER`.
- Geração do UUID pela camada de persistência.
- Autenticação das credenciais e emissão do token JWT.

### Regras de edição

- Identificação da conta pelo e-mail proveniente do contexto de autenticação.
- Ausência de ID de usuário no corpo ou na rota de edição.
- Validação e normalização do novo e-mail.
- Rejeição do novo e-mail quando utilizado por outra conta.
- Atualização somente do nome e do e-mail, preservando a senha cadastrada.
- Retorno do UUID, nome e e-mail, sem exposição da senha ou de seu hash.

### Regras de visualização

- Consulta da conta pelo e-mail do usuário autenticado.
- Retorno somente do UUID, nome e e-mail.
- Rejeição da consulta quando o usuário autenticado não for encontrado.
- Operação estritamente de leitura, sem alteração de dados.

### Comando executado

```text
Tests run: 44, Failures: 44, Errors: 0, Skipped: 0
BUILD FAILURE
```

### Resultado

Os 11 cenários pertencentes ao fluxo de usuário passaram. A execução completa
permaneceu vermelha porque as implementações de projeto, tarefa, link e tag ainda
não haviam sido realizadas:

```text
Tests run: 44, Failures: 33, Errors: 0, Skipped: 0
BUILD FAILURE
```

O resultado representa um **GREEN parcial**: todos os comportamentos da primeira
implementação estão verdes, enquanto os 33 cenários das próximas implementações
continuam no estado RED.

### Evidência do GREEN parcial

- [`results/GREEN_TDD_Impl1.png`](./results/GREEN_TDD_Impl1.png)

### Próxima etapa

Implementar o fluxo de criação e visualização de projetos e a adição de tarefas,
mantendo os 11 cenários de usuário verdes durante a evolução da suíte.

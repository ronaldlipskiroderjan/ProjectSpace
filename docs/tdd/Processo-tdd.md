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

- [`results/RED/RED_TDD_FASE1_1.png`](results/RED/RED_TDD_FASE1_1.png)
- [`results/RED/RED_TDD_FASE1_2.png`](results/RED/RED_TDD_FASE1_2.png)

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

##

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

- [`results/GREEN/GREEN_TDD_Impl1.png`](results/GREEN/GREEN_TDD_Impl1.png)

## GREEN parcial: Implementação 2 (projeto e adição de tarefa)

- **Status:** concluída
- **Data:** 16/11/2026
- **Hora de conclusão:** 00:00 (BRT/UTC-3)

### Escopo implementado

A segunda implementação GREEN concluiu os fluxos de:

- criação de projeto;
- adição de tarefa a um projeto;
- visualização dos projetos do usuário autenticado.

Os métodos autenticados passaram a receber o `username` fornecido por
`Authentication.getName()`. A resolução da entidade autenticada ficou
centralizada em `UsuarioService.buscarUsuarioAutenticado`, e as Services de
projeto e tarefa usam o `usuarioId` dessa entidade para filtrar dados e validar
propriedade.

### Regras de criação de projeto

- Identificação do proprietário pelo usuário autenticado, sem receber UUID de
  usuário no corpo ou na rota.
- Rejeição de título ausente ou em branco.
- Aceitação somente das prioridades previstas pelo domínio.
- Associação da entidade `UsuarioEntity` autenticada ao novo projeto.
- Persistência de uma nova `ProjetoEntity`, deixando a geração do UUID para a
  camada de persistência.
- Manutenção do retorno `void` no método de criação.

### Regras de adição de tarefa

- Validação do título obrigatório.
- Consulta do projeto informado no Request.
- Rejeição de projeto inexistente.
- Validação de que o projeto pertence ao usuário autenticado.
- Rejeição da operação quando o projeto pertence a outro usuário.
- Associação da entidade `ProjetoEntity` carregada à nova tarefa.
- Cópia de título, prioridade, status e data limite para a entidade.
- Persistência da nova tarefa com UUID gerado pela camada de persistência.
- Manutenção do retorno `void` no método de criação.

### Regras de visualização de projetos

- Consulta do usuário pelo `username` autenticado.
- Filtro por `usuarioId` usando
  `projetoRepository.findAllByUsuario_UsuarioId`.
- Conversão das entidades para `ProjetoResponseDTO` em método dedicado.
- Retorno somente dos projetos pertencentes ao usuário autenticado.
- Operação estritamente de leitura, sem chamadas de persistência.

### Comando executado

```bash
LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
docker compose run --rm --build cucumber
```

### Resultado

Os 10 cenários pertencentes à segunda implementação passaram, e os 11 cenários
da implementação de usuário continuaram verdes. A suíte passou a ter 21 dos 44
cenários aprovados:

```text
Tests run: 44, Failures: 23, Errors: 0, Skipped: 0
BUILD FAILURE
```

Esse resultado representa o segundo **GREEN parcial** do projeto: todo o escopo
da Pessoa 2 está verde sem regressão no escopo da Pessoa 1.

### Evidência do GREEN parcial

- [`results/GREEN/GREEN_TDD_Impl2.png`](results/GREEN/GREEN_TDD_Impl2.png)

### Próxima etapa

Implementar os fluxos de edição, exclusão e visualização de tarefas, mantendo
verdes os 21 cenários já concluídos nas Implementações 1 e 2.

## GREEN parcial: Implementação 3 (edição, exclusão e visualização de tarefas)

- **Status:** concluída
- **Data:** 16/11/2026
- **Hora de conclusão:** 11:43 (BRT/UTC-3)

### Escopo implementado

A terceira implementação GREEN concluiu os fluxos de:

- edição de tarefa;
- exclusão de tarefa;
- visualização das tarefas de um projeto.

As operações autenticadas identificam o usuário pelo `username`, resolvem a
entidade autenticada por meio de `UsuarioService.buscarUsuarioAutenticado` e
validam a propriedade do projeto antes de editar, excluir ou listar tarefas.

### Regras de edição de tarefa

- Consulta da tarefa pelo UUID informado.
- Rejeição da operação quando a tarefa não existe.
- Validação de que o projeto atual pertence ao usuário autenticado.
- Rejeição de título ausente ou em branco.
- Validação da propriedade do projeto de destino quando ele é alterado.
- Atualização de projeto, título, prioridade, status e data limite.
- Preservação do UUID original da tarefa.
- Persistência da mesma entidade e retorno dos dados atualizados em
  `TarefaResponseDTO`.
- Ausência de persistência quando a operação é inválida.

### Regras de exclusão de tarefa

- Consulta da tarefa pelo UUID informado.
- Rejeição da operação quando a tarefa não existe.
- Validação de que o projeto da tarefa pertence ao usuário autenticado.
- Exclusão somente da tarefa solicitada por meio de `deleteById`.
- Ausência de alteração no projeto durante a exclusão.
- Rejeição de uma segunda tentativa de exclusão da mesma tarefa, sem nova
  chamada a `deleteById`.

### Regras de visualização de tarefas

- Consulta do projeto pelo UUID informado.
- Validação de que o projeto pertence ao usuário autenticado.
- Rejeição da consulta quando o projeto pertence a outro usuário.
- Consulta somente das tarefas vinculadas ao projeto solicitado.
- Conversão das entidades para `TarefaResponseDTO`.
- Operação estritamente de leitura, sem chamadas de persistência ou exclusão.

### Comando executado

```bash
LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
docker compose run --rm cucumber
```

### Resultado

Os 10 cenários pertencentes à terceira implementação passaram. Os 21 cenários
das Implementações 1 e 2 continuaram verdes, totalizando 31 dos 44 cenários
aprovados:

```text
Tests run: 44, Failures: 13, Errors: 0, Skipped: 0
BUILD FAILURE
```

Esse resultado representa o terceiro **GREEN parcial** do projeto: todo o escopo
da Pessoa 3 está verde sem regressão nos escopos das Pessoas 1 e 2. Os 13
cenários restantes pertencem aos fluxos de link e tag da próxima implementação.

### Evidência do GREEN parcial

- [`results/GREEN/GREEN_TDD_Impl3.png`](results/GREEN/GREEN_TDD_Impl3.png)

### Próxima etapa

Implementar os fluxos de adição e visualização de links e de criação, vínculo e
visualização de tags, mantendo verdes os 31 cenários já concluídos nas
Implementações 1, 2 e 3.

## GREEN total: Implementação 4 (links e tags)

- **Status:** concluída
- **Data:** 16/11/2026
- **Hora de conclusão:** 15:32 (BRT/UTC-3)

### Escopo implementado

A quarta implementação GREEN concluiu os fluxos de:

- adição de link a um projeto;
- visualização dos links de um projeto;
- criação de tag;
- vínculo de tag a um projeto;
- visualização das tags de um projeto.

As operações autenticadas identificam o usuário pelo `username`, resolvem a
entidade autenticada por meio de `UsuarioService.buscarUsuarioAutenticado` e
validam a propriedade do projeto antes de adicionar ou visualizar links e de
vincular ou visualizar tags.

### Regras de adição e visualização de links

- Consulta do projeto pelo UUID informado.
- Rejeição da operação quando o projeto não existe.
- Validação de que o projeto pertence ao usuário autenticado.
- Rejeição da operação quando o projeto pertence a outro usuário.
- Associação do novo link à entidade do projeto consultado.
- Preservação da URL exatamente como recebida, sem validação ou alteração de
  formato.
- Persistência do novo link com UUID gerado pela camada de persistência.
- Consulta somente dos links vinculados ao projeto solicitado.
- Conversão das entidades para `LinkResponseDTO` durante a visualização.
- Operação de visualização estritamente de leitura, sem chamadas de
  persistência.

### Regras de criação de tag

- Validação do nome obrigatório.
- Normalização do nome com remoção dos espaços das extremidades e conversão
  para letras minúsculas.
- Consulta por nome sem diferenciação entre letras maiúsculas e minúsculas.
- Rejeição da criação quando já existe uma tag com o mesmo nome.
- Persistência da nova tag com UUID gerado pela camada de persistência.
- Ausência de persistência quando a operação é inválida.

### Regras de vínculo e visualização de tags

- Consulta do projeto e validação de que ele pertence ao usuário autenticado.
- Consulta da tag existente pelo UUID informado.
- Rejeição do vínculo quando a tag não existe.
- Rejeição de uma tag já vinculada ao mesmo projeto.
- Permissão para compartilhar a mesma tag entre projetos diferentes.
- Inclusão da entidade existente na coleção de tags e persistência do projeto.
- Leitura das tags diretamente da coleção do projeto durante a visualização.
- Conversão das entidades para `TagResponseDTO`.
- Operação de visualização estritamente de leitura, sem salvar o projeto ou as
  tags.

### Comando executado

```bash
LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
docker compose run --rm cucumber
```

### Resultado

Os 13 cenários pertencentes à quarta implementação passaram, e os 31 cenários
das Implementações 1, 2 e 3 continuaram verdes. Com isso, todos os 44 cenários
da suíte foram aprovados:

```text
Tests run: 44, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Esse resultado representa o **GREEN total** do projeto: todo o escopo da Pessoa
4 está verde, sem regressão nos escopos das Pessoas 1, 2 e 3.

### Evidência do GREEN total

- [`results/GREEN/GREEN_TDD_Impl4.png`](results/GREEN/GREEN_TDD_Impl4.png)

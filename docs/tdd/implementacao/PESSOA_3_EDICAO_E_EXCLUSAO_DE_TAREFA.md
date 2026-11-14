# Pessoa 3 — Editar e excluir tarefa

## Responsabilidade

Esta pessoa implementará, nesta ordem:

1. `editar_tarefa.feature` em `TarefaService.editarTarefa`.
2. `excluir_tarefa.feature` em `TarefaService.excluirTarefa`.
3. `visualizar_tarefas.feature` em `TarefaService.listarTarefas`.

Arquivos principais:

- `service/TarefaService.java`
- `database/model/TarefaEntity.java`
- `database/model/ProjetoEntity.java`
- `database/repository/TarefaRepository.java`
- `database/repository/ProjetoRepository.java`
- `TarefaRequestDTO` e `TarefaResponseDTO`

Adicione `TarefaRepository` e `ProjetoRepository` como dependências `final` de `TarefaService`, usando a injeção por construtor do Lombok.

## 1. Editar tarefa

Implemente `TarefaService.editarTarefa(UUID usuarioAutenticadoId, UUID tarefaId, TarefaRequestDTO request)`:

1. Buscar a tarefa com `tarefaRepository.findById(tarefaId)`.
2. Se ela não existir, lançar `TAREFA_NAO_ENCONTRADA`.
3. Buscar o projeto ao qual a tarefa pertence usando `tarefa.getProjetoId()`.
4. Conferir se `projeto.getUsuarioId()` é igual ao usuário autenticado.
5. Se o projeto pertencer a outro usuário, lançar `ACESSO_NEGADO`.
6. Se o título for nulo ou estiver em branco, lançar `TITULO_OBRIGATORIO`.
7. Atualizar `projetoId`, `titulo`, `prioridade`, `status` e `dataLimite` na entidade encontrada.
8. Preservar o `tarefaId` original.
9. Salvar a mesma entidade com `tarefaRepository.save(tarefa)`.
10. Retornar `TarefaResponseDTO` com todos os valores atualizados.

## 2. Excluir tarefa

Implemente `TarefaService.excluirTarefa(UUID usuarioAutenticadoId, UUID tarefaId)`:

1. Buscar a tarefa pelo ID.
2. Se não existir, lançar `TAREFA_NAO_ENCONTRADA`.
3. Buscar o projeto usando o `projetoId` da tarefa.
4. Validar se o projeto pertence ao usuário autenticado.
5. Se pertencer a outro usuário, lançar `ACESSO_NEGADO`.
6. Excluir somente pelo ID solicitado com `tarefaRepository.deleteById(tarefaId)`.
7. Não excluir nem salvar o projeto durante essa operação.

Na segunda tentativa de excluir a mesma tarefa, o Repository retornará vazio. A Service deve lançar `TAREFA_NAO_ENCONTRADA` e não chamar `deleteById` novamente.

## 3. Visualizar tarefas

Implemente `TarefaService.listarTarefas(UUID usuarioAutenticadoId, UUID projetoId)`:

1. Buscar o projeto pelo ID.
2. Validar se o projeto pertence ao usuário autenticado.
3. Para projeto de outro usuário, lançar `ACESSO_NEGADO`.
4. Consultar `tarefaRepository.findAllByProjetoId(projetoId)`.
5. Converter as entidades em `TarefaResponseDTO` e retornar a lista.
6. Não chamar `save` nem `deleteById` durante a consulta.

## Trabalho compartilhado

`TarefaService.java` também será alterado pela Pessoa 2. Para reduzir conflitos:

1. Não altere o comportamento de `adicionarTarefa`.
2. Evite reformatação geral do arquivo.
3. Faça commits separados para edição e exclusão.
4. Reaproveite as dependências adicionadas pela Pessoa 2 ao integrar as branches.

Se desejar extrair uma validação privada de propriedade do projeto, combine antes com a Pessoa 2 para que o helper atenda aos três métodos da Service.

## Erros de negócio

As mensagens precisam ser exatamente as descritas nos cenários:

```java
throw new IllegalArgumentException("TAREFA_NAO_ENCONTRADA");
```

## Critério de conclusão

- Os quatro cenários de edição passam.
- Os quatro cenários de exclusão passam.
- Os dois cenários de visualização de tarefas passam.
- A edição preserva o ID e retorna todos os campos esperados.
- A exclusão chama `deleteById` somente quando autorizada.
- Operações inválidas não chamam `save` nem `deleteById`.
- A suíte completa continua compilando.

Execute:

```bash
docker compose run --rm cucumber
```

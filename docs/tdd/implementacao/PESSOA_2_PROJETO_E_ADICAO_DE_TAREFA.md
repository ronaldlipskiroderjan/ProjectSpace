# Pessoa 2 — Criar projeto e adicionar tarefa

## Responsabilidade

Esta pessoa implementará, nesta ordem:

1. `criar_projeto.feature` em `ProjetoService.criarProjeto`.
2. `adicionar_tarefa.feature` em `TarefaService.adicionarTarefa`.
3. `visualizar_projetos.feature` em `ProjetoService.listarProjetos`.

Arquivos principais:

- `service/ProjetoService.java`
- `service/TarefaService.java`
- `database/model/ProjetoEntity.java`
- `database/model/TarefaEntity.java`
- `database/repository/ProjetoRepository.java`
- `database/repository/TarefaRepository.java`
- `ProjetoRequestDTO` e `TarefaRequestDTO`

Adicione os Repositories como campos `final` das Services e use a injeção por construtor já fornecida por `@RequiredArgsConstructor`.

## 1. Criar projeto

Implemente `ProjetoService.criarProjeto(UUID usuarioAutenticadoId, ProjetoRequestDTO request)`:

1. Se `usuarioAutenticadoId` for nulo, lançar `NAO_AUTENTICADO`.
2. Se o título for nulo ou estiver em branco, lançar `TITULO_OBRIGATORIO`.
3. Aceitar somente as prioridades definidas pelo domínio, incluindo `baixa`, `média` e `alta`.
4. Para outro valor, como `urgente`, lançar `PRIORIDADE_INVALIDA`.
5. Criar `ProjetoEntity` com `projetoId` nulo.
6. Copiar título, prioridade e status do Request.
7. Definir `usuarioId` com o ID autenticado, nunca com um valor recebido do cliente.
8. Chamar `projetoRepository.save(projeto)` uma única vez.
9. O método deve permanecer `void`.

## 2. Adicionar tarefa

Implemente `TarefaService.adicionarTarefa(UUID usuarioAutenticadoId, TarefaRequestDTO request)`:

1. Validar o título; se estiver nulo ou em branco, lançar `TITULO_OBRIGATORIO`.
2. Buscar o projeto com `projetoRepository.findById(request.projetoId())`.
3. Se o projeto não existir, lançar `PROJETO_NAO_ENCONTRADO`.
4. Comparar `projeto.getUsuarioId()` com `usuarioAutenticadoId`.
5. Se forem diferentes, lançar `ACESSO_NEGADO`.
6. Criar `TarefaEntity` com `tarefaId` nulo.
7. Copiar `projetoId`, `titulo`, `prioridade`, `status` e `dataLimite`.
8. Salvar usando `tarefaRepository.save(tarefa)`.
9. O método deve permanecer `void`.

O ID de uma entidade nova deve continuar nulo no teste. A geração do UUID será responsabilidade da persistência quando o mapeamento JPA definitivo for configurado.

## 3. Visualizar projetos

Implemente `ProjetoService.listarProjetos(UUID usuarioAutenticadoId)`:

1. Se o ID autenticado for nulo, lançar `NAO_AUTENTICADO`.
2. Consultar `projetoRepository.findAllByUsuarioId(usuarioAutenticadoId)`.
3. Converter cada entidade em `ProjetoResponseDTO`.
4. Retornar somente projetos do usuário autenticado.
5. Não chamar métodos de escrita durante a consulta.

## Trabalho compartilhado

`TarefaService.java` também será alterado pela Pessoa 3. Para reduzir conflitos:

1. Faça um commit separado somente para `ProjetoService`.
2. Faça outro commit para `adicionarTarefa` e as dependências compartilhadas.
3. Não remova nem reformate os métodos `editarTarefa` e `excluirTarefa`.
4. Combine com a Pessoa 3 quem adicionará os campos compartilhados `TarefaRepository` e `ProjetoRepository`.

## Erros de negócio

Use mensagens exatamente iguais às dos cenários. Nesta etapa, uma exceção de runtime é suficiente:

```java
throw new IllegalArgumentException("PROJETO_NAO_ENCONTRADO");
```

## Critério de conclusão

- Os quatro cenários de criação de projeto passam.
- Os quatro cenários de adição de tarefa passam.
- Os dois cenários de visualização de projetos passam.
- Projetos e tarefas novos são salvos com seus IDs nulos.
- Nenhuma operação inválida chama `save`.
- Os dois métodos continuam retornando `void`.
- A suíte completa continua compilando.

Execute:

```bash
docker compose run --rm cucumber
```

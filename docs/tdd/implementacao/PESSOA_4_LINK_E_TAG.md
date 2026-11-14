# Pessoa 4 — Adicionar link e adicionar tag

## Responsabilidade

Esta pessoa implementará, nesta ordem:

1. `adicionar_link.feature` em `LinkService.adicionarLink`.
2. `adicionar_tag.feature` em `TagService.criarTag` e `TagService.vincularTagAoProjeto`.
3. `visualizar_links.feature` em `LinkService.listarLinks`.
4. `visualizar_tags.feature` em `TagService.listarTags`.

Arquivos principais:

- `service/LinkService.java`
- `service/TagService.java`
- `database/model/LinkEntity.java`
- `database/model/TagEntity.java`
- `database/model/ProjetoEntity.java`
- `database/repository/LinkRepository.java`
- `database/repository/TagRepository.java`
- `database/repository/ProjetoRepository.java`
- `LinkRequestDTO` e `TagRequestDTO`

## 1. Adicionar link ao projeto

Implemente `LinkService.adicionarLink(UUID usuarioAutenticadoId, LinkRequestDTO request)`:

1. Buscar o projeto com `projetoRepository.findById(request.projetoId())`.
2. Se o projeto não existir, lançar `PROJETO_NAO_ENCONTRADO`.
3. Comparar o proprietário do projeto com `usuarioAutenticadoId`.
4. Se forem diferentes, lançar `ACESSO_NEGADO`.
5. Criar `LinkEntity` com `linkId` nulo.
6. Copiar `projetoId` e `url` para a entidade.
7. Preservar a URL exatamente como foi recebida. Não validar nem modificar seu formato.
8. Salvar usando `linkRepository.save(link)`.
9. O método deve permanecer `void`.

## 2. Criar tag

Implemente `TagService.criarTag(TagRequestDTO request)`:

1. Se o nome for nulo ou estiver em branco, lançar `NOME_OBRIGATORIO`.
2. Normalizar o nome com `trim()` e `toLowerCase(Locale.ROOT)`.
3. Consultar `tagRepository.findByNomeIgnoreCase(nomeNormalizado)`.
4. Se já existir, lançar `TAG_DUPLICADA`.
5. Criar `TagEntity` com `tagId` nulo e nome normalizado.
6. Salvar usando `tagRepository.save(tag)`.
7. O método deve permanecer `void`.

## 3. Vincular tag a projeto

O vínculo faz parte da funcionalidade de adicionar tags. Implemente `TagService.vincularTagAoProjeto(UUID usuarioAutenticadoId, UUID projetoId, UUID tagId)`:

1. Buscar o projeto por `projetoId`.
2. Validar que o projeto pertence ao usuário autenticado.
3. Buscar a tag por `tagId`.
4. Consultar `projeto.getTags()`.
5. Se a tag já estiver nesse mesmo projeto, lançar `TAG_JA_VINCULADA`.
6. Uma tag vinculada a outro projeto ainda pode ser compartilhada com o projeto atual.
7. Adicionar a entidade existente ao conjunto de tags do projeto.
8. Salvar o projeto com `projetoRepository.save(projeto)`.
9. Não criar nem salvar uma nova tag durante o vínculo.

Use `HashSet`/`Set` sem substituir a coleção existente do projeto. Quando o mapeamento JPA definitivo for implementado, preserve essa regra com uma associação apropriada entre projeto e tag.

## 4. Visualizar links

Implemente `LinkService.listarLinks(UUID usuarioAutenticadoId, UUID projetoId)`:

1. Buscar o projeto e validar seu proprietário.
2. Para projeto de outro usuário, lançar `ACESSO_NEGADO`.
3. Consultar `linkRepository.findAllByProjetoId(projetoId)`.
4. Converter as entidades em `LinkResponseDTO` e retornar a lista.
5. Não executar operações de escrita.

## 5. Visualizar tags

Implemente `TagService.listarTags(UUID usuarioAutenticadoId, UUID projetoId)`:

1. Buscar o projeto e validar seu proprietário.
2. Para projeto de outro usuário, lançar `ACESSO_NEGADO`.
3. Ler `projeto.getTags()`.
4. Converter as entidades em `TagResponseDTO` e retornar a lista.
5. Não salvar o projeto nem as tags durante a consulta.

## Erros de negócio

Use mensagens exatamente iguais às esperadas:

```java
throw new IllegalArgumentException("TAG_JA_VINCULADA");
```

## Critério de conclusão

- Os quatro cenários de link passam.
- Os cinco cenários de tag passam.
- Os quatro cenários de visualização de links e tags passam.
- A URL é preservada sem validação de formato.
- O nome da tag é salvo normalizado.
- Uma tag pode ser compartilhada entre projetos, mas não duplicada no mesmo projeto.
- Operações inválidas não chamam `save`.
- A criação de link e tag continua retornando `void`.
- A suíte completa continua compilando.

Execute:

```bash
docker compose run --rm cucumber
```

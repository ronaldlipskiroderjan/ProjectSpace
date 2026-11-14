# Testes BDD do ProjectSpace

Esta suíte usa Cucumber para testar diretamente as regras da camada `Service`, sem iniciar o Spring, servidor HTTP ou banco de dados.

## Fluxo do teste

```text
Arquivo .feature
       ↓
Classe Steps
       ↓
RequestDTO → Service real
                  ↓
          Repository mockado
                  ↓
        Entity capturada e validada
```

1. O arquivo `.feature` descreve o cenário com `Dado`, `Quando`, `Então` e `E`.
2. O `Dado` prepara o RequestDTO, o usuário autenticado e os retornos dos repositórios mockados.
3. O `Quando` chama diretamente o método da Service.
4. O `Então` verifica a regra de negócio, a Entity enviada ao repositório e se houve ou não persistência.

O cenário completo do `.feature` é o caso de teste. Um mesmo método `@Quando`, como `crio o projeto`, pode ser reutilizado em vários cenários porque a ação é a mesma; o que muda são os dados preparados e o resultado esperado.

## Onde está cada coisa

```text
src/test/
├── java/com/ProjectSpace/bdd/
│   ├── CucumberTest.java          # Executa todos os arquivos .feature
│   ├── README.md                  # Este guia
│   └── steps/
│       ├── UsuarioSteps.java      # Cadastro e edição de usuário
│       ├── ProjetoSteps.java      # Criação de projeto
│       ├── TarefaSteps.java       # Criação, edição e exclusão de tarefa
│       ├── LinkProjetoSteps.java  # Criação de link de projeto
│       ├── TagSteps.java          # Criação e vínculo de tags
│       └── VisualizacaoSteps.java # Consultas de perfil, projetos, tarefas, links e tags
└── resources/features/
    ├── criar_usuario.feature
    ├── editar_usuario.feature
    ├── criar_projeto.feature
    ├── adicionar_tarefa.feature
    ├── editar_tarefa.feature
    ├── excluir_tarefa.feature
    ├── adicionar_link.feature
    ├── adicionar_tag.feature
    ├── visualizar_perfil.feature
    ├── visualizar_projetos.feature
    ├── visualizar_tarefas.feature
    ├── visualizar_links.feature
    └── visualizar_tags.feature
```

## O que é real e o que é mock

- A Service é a classe real que está sendo testada.
- Os Repositories, o `PasswordEncoder` e o `TokenProvider` são mocks.
- Os DTOs são objetos reais; entidades existentes podem ser fixtures ou mocks, e a Entity criada pela Service é capturada no `save`.
- Não há chamada HTTP, controller, banco real ou `@SpringBootTest`.

O `@InjectMocks` cria a Service e injeta nela as dependências marcadas com `@Mock`:

```java
@Mock
private ProjetoRepository projetoRepository;

@InjectMocks
private ProjetoService projetoService;
```

## Como validar métodos que retornam `void`

As criações de projeto, tarefa, link e tag não retornam dados. O sucesso é comprovado pelo efeito produzido:

```java
verify(projetoRepository).save(captor.capture());
assertThat(captor.getValue()).extracting("titulo").isEqualTo("Projeto");
```

Nos cenários inválidos, o teste verifica o erro e garante que nada foi salvo:

```java
assertThat(erro).hasMessage("TITULO_OBRIGATORIO");
verify(projetoRepository, never()).save(any(ProjetoEntity.class));
```

O cadastro de usuário é a exceção: retorna somente `token` e `tempoExpiracao`. Senha e hash nunca devem aparecer no Response.

## UUIDs nos cenários

Os UUIDs escritos nos arquivos `.feature` são dados fixos de teste para entidades que já existem, como o usuário autenticado ou um projeto consultado.

Eles não representam o valor que será gerado em produção. Uma Entity nova não recebe ID pelo Request; seu UUID será gerado pelo Hibernate com `GenerationType.UUID` quando houver persistência real.

## Request e Response

- Os dados de entrada são convertidos em classes `*RequestDTO`.
- Os métodos de criação retornam `void`, exceto o cadastro de usuário.
- O cadastro retorna `TokenResponseDTO` com `token` e `tempoExpiracao`.
- Os cenários de edição podem validar seus respectivos `*ResponseDTO`.
- As consultas retornam `*ResponseDTO` ou listas de `*ResponseDTO`, nunca Entities.
- Toda consulta valida que nenhum `save` ou `deleteById` foi executado.

## Como executar com Docker

O serviço `cucumber` usa uma imagem com Maven e Java 21. Ele não inicia a aplicação nem o PostgreSQL, pois os repositórios usados pelos Steps são mocks.

No Linux, informe seu usuário e grupo para que os relatórios em `target/` não sejam criados como `root`:

```bash
LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) docker compose run --rm --build cucumber
```

No Windows ou macOS, basta executar:

```bash
docker compose run --rm --build cucumber
```

Depois da primeira construção, durante o ciclo TDD, o comando pode ser reduzido para:

```bash
docker compose run --rm cucumber
```

O código do projeto é montado em `/workspace`, portanto alterações nos arquivos Java e `.feature` são usadas na execução seguinte sem reconstruir a imagem. As dependências Maven são armazenadas na camada de cache da imagem.

No Fedora, o sufixo `:Z` do volume concede ao container o contexto SELinux necessário para ler o projeto montado.

Enquanto a implementação ainda não existir, uma falha de compilação ou de cenário é o resultado vermelho esperado do ciclo TDD. O comando terminará com código diferente de zero e passará quando a implementação satisfizer os cenários.

Os relatórios são gerados em:

```text
target/cucumber.html
target/cucumber.json
```

Enquanto Services, DTOs, Entities e Repositories ainda não existirem, o Maven apresentará erro de compilação. Depois que esses contratos forem implementados, os cenários passarão a validar efetivamente as regras de negócio.

## Regra para novos testes

1. Adicione ou altere primeiro o cenário no `.feature` correto.
2. Reutilize Steps existentes quando a frase representar a mesma ação.
3. Crie um novo Step somente quando existir uma nova preparação, ação ou verificação.
4. Chame a Service diretamente.
5. Verifique a Entity persistida com `ArgumentCaptor`.
6. Em erros, valide a mensagem e use `never()` para garantir que nada foi salvo.
7. Não adicione controller, HTTP, banco ou contexto Spring a esta suíte.

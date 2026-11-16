<p align="center">
  <img src="icons/IconProject.png" alt="Logo do ProjectSpace" width="850">
</p>

# ProjectSpace

O ProjectSpace é uma API para organização de projetos pessoais e profissionais. A aplicação concentra informações importantes de cada projeto em um único espaço, permitindo estruturar atividades, referências e classificações sem misturar dados entre usuários.

Cada conta possui seu próprio conjunto de projetos. A partir deles, o domínio organiza tarefas, links e tags, sempre respeitando a propriedade dos dados e o contexto do usuário autenticado.

## Domínio do projeto

O modelo principal é formado por:

- **Usuários:** representam as contas autenticadas e são proprietários dos projetos.
- **Projetos:** agrupam as informações e os recursos relacionados a um objetivo.
- **Tarefas:** descrevem atividades vinculadas a um projeto, com prioridade, status e data limite.
- **Links:** armazenam referências e recursos externos associados a um projeto.
- **Tags:** classificam projetos e podem ser compartilhadas entre diferentes projetos.

As regras do domínio garantem que um usuário somente consulte ou altere recursos pertencentes aos seus próprios projetos. A identificação do usuário parte do contexto de autenticação, sem depender de um identificador enviado pelo cliente para autorizar uma operação.

## Arquitetura

O projeto segue uma arquitetura em camadas:

```text
Requisição HTTP
      ↓
Controller
      ↓
Service
      ↓
Repository
      ↓
Banco de dados
```

- **Controllers** recebem requisições, extraem o usuário autenticado e delegam o processamento.
- **Services** concentram validações, autorização, regras de negócio e conversões para DTOs.
- **Repositories** abstraem o acesso aos dados com Spring Data JPA.
- **Entities** representam o domínio persistido e seus relacionamentos.
- **DTOs** definem os dados aceitos e retornados pela API, evitando a exposição direta das entidades.

Essa separação mantém a camada HTTP simples e impede que decisões de negócio fiquem espalhadas entre controllers ou repositórios.

## Autenticação e autorização

A API utiliza autenticação baseada em JWT. O e-mail funciona como `username` da conta autenticada e é obtido por `Authentication.getName()`.

A resolução da entidade autenticada é centralizada no serviço de usuário. As demais Services utilizam o identificador dessa entidade para filtrar consultas e validar a propriedade dos projetos. Senhas são armazenadas de forma codificada e não fazem parte dos DTOs de resposta.

## Tecnologias principais

- Java 21;
- Spring Boot;
- Spring Web MVC;
- Spring Security e JWT;
- Spring Data JPA;
- PostgreSQL;
- Maven;
- JUnit, Mockito, AssertJ e Cucumber.

## Modelo de desenvolvimento

O desenvolvimento é conduzido de forma incremental com **TDD** (*Test-Driven Development*) e **BDD** (*Behavior-Driven Development*).

### Ciclo TDD

Cada incremento percorre o ciclo:

1. **RED:** um cenário ou teste descreve o comportamento desejado e inicialmente falha.
2. **GREEN:** é implementado somente o necessário para atender ao comportamento definido.
3. **REFACTOR:** o código é reorganizado sem alterar o comportamento já protegido pelos testes.

O objetivo é manter as decisões de implementação guiadas por contratos verificáveis, reduzindo ambiguidades e regressões.

### Especificação por comportamento

Os comportamentos são descritos em arquivos `.feature`, utilizando Gherkin em português. Cada cenário expressa uma regra observável do domínio por meio das etapas **Dado**, **Quando** e **Então**.

Os cenários funcionam como documentação executável. Eles determinam:

- entradas aceitas;
- resultados esperados;
- mensagens de erro de negócio;
- operações de persistência permitidas;
- restrições de autenticação e propriedade.

Os Steps conectam esses cenários às Services e utilizam mocks para isolar as regras de negócio das dependências externas.

### Desenvolvimento por incrementos

O domínio é evoluído em conjuntos pequenos de comportamentos relacionados:

1. autenticação, cadastro, edição e visualização de usuário;
2. criação e visualização de projetos e adição de tarefas;
3. edição, exclusão e visualização de tarefas;
4. criação, vínculo e visualização de links e tags.

Essa divisão reduz conflitos entre alterações paralelas e permite validar cada parte do domínio antes de avançar para a próxima.

## Princípios adotados

- Os arquivos `.feature` representam o contrato funcional.
- Regras de negócio permanecem nas Services.
- Controllers não recebem IDs de usuário para autorizar operações.
- Recursos são associados por entidades carregadas, evitando objetos parciais criados apenas com IDs.
- Consultas não executam operações de escrita.
- Operações inválidas não persistem alterações.
- DTOs de resposta expõem somente os dados necessários.
- Mensagens de erro fazem parte do comportamento esperado e devem ser preservadas.
- Novos comportamentos devem manter os cenários existentes protegidos contra regressões.

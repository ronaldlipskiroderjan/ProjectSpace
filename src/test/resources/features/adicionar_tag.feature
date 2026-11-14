# language: pt
Funcionalidade: Adicionar tags

  Cenário: Adicionar tags — Criar tag válida
    Dado os dados para criar a tag:
      | nome | Backend |

    Quando crio a tag

    Então deve salvar a tag com os valores:
      | nome | backend |
    E o método de criação da tag não deve retornar conteúdo

  Cenário: Adicionar tags — Nome vazio
    Dado os dados para criar a tag:
      | nome |  |

    Quando crio a tag

    Então a operação de tag deve ser rejeitada com "NOME_OBRIGATORIO" sem salvar

  Cenário: Adicionar tags — Duplicidade de nome normalizado
    Dado os dados para criar a tag:
      | nome | " BACKEND " |
    E existe uma tag com nome normalizado "backend" e id "30000000-0000-0000-0000-000000000001"

    Quando crio a tag

    Então a operação de tag deve ser rejeitada com "TAG_DUPLICADA" sem salvar

  Cenário: Adicionar tags — Associar tag já vinculada ao projeto
    Dado os dados para vincular a tag ao projeto:
      | projetoId | 10000000-0000-0000-0000-000000000001 |
      | tagId     | 30000000-0000-0000-0000-000000000001 |
    E existe para a tag um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"
    E existe uma tag com id "30000000-0000-0000-0000-000000000001"
    E a tag "30000000-0000-0000-0000-000000000001" já está vinculada ao projeto "10000000-0000-0000-0000-000000000001"

    Quando vinculo a tag ao projeto

    Então a operação de tag deve ser rejeitada com "TAG_JA_VINCULADA" sem salvar

  Cenário: Adicionar tags — Compartilhar tag existente entre projetos
    Dado os dados para vincular a tag ao projeto:
      | projetoId | 10000000-0000-0000-0000-000000000001 |
      | tagId     | 30000000-0000-0000-0000-000000000001 |
    E existe para a tag um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"
    E existe para a tag um projeto com id "10000000-0000-0000-0000-000000000002" e usuário "00000000-0000-0000-0000-000000000001"
    E existe uma tag com id "30000000-0000-0000-0000-000000000001"
    E a tag "30000000-0000-0000-0000-000000000001" já está vinculada ao projeto "10000000-0000-0000-0000-000000000002"

    Quando vinculo a tag ao projeto

    Então deve vincular somente a tag "30000000-0000-0000-0000-000000000001" ao projeto "10000000-0000-0000-0000-000000000001"

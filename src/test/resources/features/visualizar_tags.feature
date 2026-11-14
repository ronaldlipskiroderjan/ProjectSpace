# language: pt
Funcionalidade: Visualizar tags

  Cenário: Visualizar tags — Listar tags de projeto próprio
    Dado existe para visualização um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"
    E existem para visualização as tags do projeto:
      | tagId                                | nome     |
      | 30000000-0000-0000-0000-000000000001 | backend  |
      | 30000000-0000-0000-0000-000000000002 | frontend |

    Quando visualizo as tags do projeto "10000000-0000-0000-0000-000000000001"

    Então devem ser retornadas as tags:
      | tagId                                | nome     |
      | 30000000-0000-0000-0000-000000000001 | backend  |
      | 30000000-0000-0000-0000-000000000002 | frontend |
    E a visualização não deve alterar dados

  Cenário: Visualizar tags — Projeto de outro usuário
    Dado existe para visualização um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000002"

    Quando visualizo as tags do projeto "10000000-0000-0000-0000-000000000001"

    Então a visualização deve ser rejeitada com "ACESSO_NEGADO"
    E a visualização não deve alterar dados

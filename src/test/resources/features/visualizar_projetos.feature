# language: pt
Funcionalidade: Visualizar projetos

  Cenário: Visualizar projetos — Listar projetos do usuário autenticado
    Dado existem para visualização os projetos:
      | projetoId                           | titulo       | prioridade | status     |
      | 10000000-0000-0000-0000-000000000001 | ProjectSpace | alta       | em andamento |
      | 10000000-0000-0000-0000-000000000002 | Portfólio    | média      | planejado  |

    Quando visualizo meus projetos

    Então devem ser retornados os projetos:
      | projetoId                           | titulo       | prioridade | status     |
      | 10000000-0000-0000-0000-000000000001 | ProjectSpace | alta       | em andamento |
      | 10000000-0000-0000-0000-000000000002 | Portfólio    | média      | planejado  |
    E a visualização não deve alterar dados

  Cenário: Visualizar projetos — Usuário não autenticado
    Dado não há usuário autenticado para visualização

    Quando visualizo meus projetos

    Então a visualização deve ser rejeitada com "NAO_AUTENTICADO"
    E a visualização não deve alterar dados

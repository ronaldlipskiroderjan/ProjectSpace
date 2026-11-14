# language: pt
Funcionalidade: Visualizar tarefas

  Cenário: Visualizar tarefas — Listar tarefas de projeto próprio
    Dado existe para visualização um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"
    E existem para visualização as tarefas do projeto:
      | tarefaId                            | projetoId                           | titulo        | prioridade | status    | dataLimite |
      | 20000000-0000-0000-0000-000000000001 | 10000000-0000-0000-0000-000000000001 | Criar API     | alta       | pendente  | 2026-12-01 |
      | 20000000-0000-0000-0000-000000000002 | 10000000-0000-0000-0000-000000000001 | Criar interface | média      | planejado | 2026-12-10 |

    Quando visualizo as tarefas do projeto "10000000-0000-0000-0000-000000000001"

    Então devem ser retornadas as tarefas:
      | tarefaId                            | projetoId                           | titulo        | prioridade | status    | dataLimite |
      | 20000000-0000-0000-0000-000000000001 | 10000000-0000-0000-0000-000000000001 | Criar API     | alta       | pendente  | 2026-12-01 |
      | 20000000-0000-0000-0000-000000000002 | 10000000-0000-0000-0000-000000000001 | Criar interface | média      | planejado | 2026-12-10 |
    E a visualização não deve alterar dados

  Cenário: Visualizar tarefas — Projeto de outro usuário
    Dado existe para visualização um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000002"

    Quando visualizo as tarefas do projeto "10000000-0000-0000-0000-000000000001"

    Então a visualização deve ser rejeitada com "ACESSO_NEGADO"
    E a visualização não deve alterar dados

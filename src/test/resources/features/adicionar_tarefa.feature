# language: pt
Funcionalidade: Adicionar tarefa

  Contexto:
    Dado os dados para adicionar a tarefa:
      | projetoId  | 10000000-0000-0000-0000-000000000001 |
      | titulo     | Tarefa                               |
      | prioridade | alta                                 |
      | status     | pendente                             |
      | dataLimite | 2026-12-01                           |

  Cenário: Adicionar tarefa — Criação válida
    E existe para a tarefa um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"

    Quando adiciono a tarefa

    Então deve salvar a tarefa com os valores:
      | projetoId  | 10000000-0000-0000-0000-000000000001 |
      | titulo     | Tarefa                               |
      | prioridade | alta                                 |
      | status     | pendente                             |
      | dataLimite | 2026-12-01                           |
    E o método de criação da tarefa não deve retornar conteúdo

  Cenário: Adicionar tarefa — Título vazio
    Dado os dados para adicionar a tarefa:
      | projetoId  | 10000000-0000-0000-0000-000000000001 |
      | titulo     |                                      |
      | prioridade | alta                                 |
      | status     | pendente                             |
      | dataLimite | 2026-12-01                           |
    E existe para a tarefa um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"

    Quando adiciono a tarefa

    Então a operação de tarefa deve ser rejeitada com "TITULO_OBRIGATORIO" sem salvar

  Cenário: Adicionar tarefa — Projeto inexistente
    Quando adiciono a tarefa

    Então a operação de tarefa deve ser rejeitada com "PROJETO_NAO_ENCONTRADO" sem salvar

  Cenário: Adicionar tarefa — Projeto de outro usuário
    E existe para a tarefa um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000002"

    Quando adiciono a tarefa

    Então a operação de tarefa deve ser rejeitada com "ACESSO_NEGADO" sem salvar

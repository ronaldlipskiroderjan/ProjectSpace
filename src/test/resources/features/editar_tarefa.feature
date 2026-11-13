# language: pt
Funcionalidade: Editar tarefa

  Contexto:
    Dado os dados para editar a tarefa:
      | projetoId  | 10000000-0000-0000-0000-000000000001 |
      | tarefaId   | 20000000-0000-0000-0000-000000000001 |
      | titulo     | Novo título                         |
      | prioridade | baixa                                |
      | status     | concluída                           |
      | dataLimite | 2026-12-01                           |

  Cenário: Editar tarefa — Edição válida
    E existe para a tarefa um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"
    E existe uma tarefa com id "20000000-0000-0000-0000-000000000001" no projeto "10000000-0000-0000-0000-000000000001"

    Quando edito a tarefa

    Então deve salvar a tarefa com os valores:
      | projetoId  | 10000000-0000-0000-0000-000000000001 |
      | tarefaId   | 20000000-0000-0000-0000-000000000001 |
      | titulo     | Novo título                         |
      | prioridade | baixa                                |
      | status     | concluída                           |
      | dataLimite | 2026-12-01                           |
    E a resposta da tarefa deve conter:
      | projetoId  | 10000000-0000-0000-0000-000000000001 |
      | tarefaId   | 20000000-0000-0000-0000-000000000001 |
      | titulo     | Novo título                         |
      | prioridade | baixa                                |
      | status     | concluída                           |
      | dataLimite | 2026-12-01                           |

  Cenário: Editar tarefa — Título vazio
    Dado os dados para editar a tarefa:
      | projetoId  | 10000000-0000-0000-0000-000000000001 |
      | tarefaId   | 20000000-0000-0000-0000-000000000001 |
      | titulo     |                                      |
      | prioridade | alta                                 |
      | status     | pendente                             |
      | dataLimite | 2026-12-01                           |
    E existe para a tarefa um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"
    E existe uma tarefa com id "20000000-0000-0000-0000-000000000001" no projeto "10000000-0000-0000-0000-000000000001"

    Quando edito a tarefa

    Então a operação de tarefa deve ser rejeitada com "TITULO_OBRIGATORIO" sem salvar

  Cenário: Editar tarefa — Tarefa inexistente
    Quando edito a tarefa

    Então a operação de tarefa deve ser rejeitada com "TAREFA_NAO_ENCONTRADA" sem salvar

  Cenário: Editar tarefa — Tentativa de editar tarefa de terceiro
    E existe para a tarefa um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000002"
    E existe uma tarefa com id "20000000-0000-0000-0000-000000000001" no projeto "10000000-0000-0000-0000-000000000001"

    Quando edito a tarefa

    Então a operação de tarefa deve ser rejeitada com "ACESSO_NEGADO" sem salvar

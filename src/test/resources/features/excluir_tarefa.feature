# language: pt
Funcionalidade: Excluir tarefa

  Contexto:
    Dado os dados para excluir a tarefa:
      | tarefaId | 20000000-0000-0000-0000-000000000001 |

  Cenário: Excluir tarefa — Exclusão válida
    E existe para a tarefa um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"
    E existe uma tarefa com id "20000000-0000-0000-0000-000000000001" no projeto "10000000-0000-0000-0000-000000000001"

    Quando excluo a tarefa

    Então deve remover somente a tarefa "20000000-0000-0000-0000-000000000001"

  Cenário: Excluir tarefa — Tarefa inexistente
    Quando excluo a tarefa

    Então a operação de tarefa deve ser rejeitada com "TAREFA_NAO_ENCONTRADA" sem salvar

  Cenário: Excluir tarefa — Tentativa de excluir tarefa de terceiro
    E existe para a tarefa um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000002"
    E existe uma tarefa com id "20000000-0000-0000-0000-000000000001" no projeto "10000000-0000-0000-0000-000000000001"

    Quando excluo a tarefa

    Então a operação de tarefa deve ser rejeitada com "ACESSO_NEGADO" sem salvar

  Cenário: Excluir tarefa — Segunda exclusão da mesma tarefa
    E existe para a tarefa um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"
    E existe uma tarefa com id "20000000-0000-0000-0000-000000000001" no projeto "10000000-0000-0000-0000-000000000001"

    Quando excluo a tarefa duas vezes

    Então a operação de tarefa deve ser rejeitada com "TAREFA_NAO_ENCONTRADA" sem salvar

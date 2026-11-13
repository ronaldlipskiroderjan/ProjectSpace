# language: pt
Funcionalidade: Editar informações da conta

  Cenário: Editar informações da conta — Atualização válida
    Dado os dados para editar a conta de usuário:
      | usuarioId | 00000000-0000-0000-0000-000000000001 |
      | nome      | Ana Maria                            |
      | email     | nova@example.com                     |
    E existe um usuário com id "00000000-0000-0000-0000-000000000001"

    Quando edito a conta de usuário

    Então deve salvar o usuário com os valores:
      | usuarioId | 00000000-0000-0000-0000-000000000001 |
      | nome      | Ana Maria                            |
      | email     | nova@example.com                     |
    E a resposta do usuário deve conter:
      | usuarioId | 00000000-0000-0000-0000-000000000001 |
      | nome      | Ana Maria                            |
      | email     | nova@example.com                     |

  Cenário: Editar informações da conta — E-mail inválido
    Dado os dados para editar a conta de usuário:
      | usuarioId | 00000000-0000-0000-0000-000000000001 |
      | nome      | Ana                                  |
      | email     | invalido                             |
    E existe um usuário com id "00000000-0000-0000-0000-000000000001"

    Quando edito a conta de usuário

    Então a operação de usuário deve ser rejeitada com "EMAIL_INVALIDO" sem salvar

  Cenário: Editar informações da conta — E-mail usado por outra conta
    Dado os dados para editar a conta de usuário:
      | usuarioId | 00000000-0000-0000-0000-000000000001 |
      | nome      | Ana                                  |
      | email     | outra@example.com                    |
    E existe um usuário com id "00000000-0000-0000-0000-000000000001"
    E existe outro usuário com e-mail normalizado "outra@example.com" e id "00000000-0000-0000-0000-000000000002"

    Quando edito a conta de usuário

    Então a operação de usuário deve ser rejeitada com "EMAIL_DUPLICADO" sem salvar

  Cenário: Editar informações da conta — Tentativa de editar outra conta
    Dado os dados para editar a conta de usuário:
      | usuarioId | 00000000-0000-0000-0000-000000000002 |
      | nome      | Ana                                  |
      | email     | nova@example.com                     |
    E existe um usuário com id "00000000-0000-0000-0000-000000000002"

    Quando edito a conta de usuário

    Então a operação de usuário deve ser rejeitada com "ACESSO_NEGADO" sem salvar

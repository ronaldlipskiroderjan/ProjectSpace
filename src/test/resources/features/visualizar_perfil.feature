# language: pt
Funcionalidade: Visualizar perfil

  Cenário: Visualizar perfil — Consulta válida
    Dado existe para visualização um usuário com os valores:
      | usuarioId | 00000000-0000-0000-0000-000000000001 |
      | nome      | Ana                                  |
      | email     | ana@example.com                      |

    Quando visualizo meu perfil

    Então o perfil retornado deve conter:
      | usuarioId | 00000000-0000-0000-0000-000000000001 |
      | nome      | Ana                                  |
      | email     | ana@example.com                      |
    E a visualização não deve alterar dados

  Cenário: Visualizar perfil — Usuário inexistente
    Quando visualizo meu perfil

    Então a visualização deve ser rejeitada com "USUARIO_NAO_ENCONTRADO"
    E a visualização não deve alterar dados

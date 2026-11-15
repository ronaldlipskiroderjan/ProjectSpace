# language: pt
Funcionalidade: Criar conta de usuário

  Cenário: Criar conta de usuário — Cadastro válido
    Dado os dados para criar a conta de usuário:
      | nome  | Ana             |
      | email | ana@example.com |
      | senha | 12345678        |

    Quando crio a conta de usuário

    Então deve salvar o usuário com os valores:
      | nome      | Ana             |
      | email     | ana@example.com |
      | senhaHash | hash-seguro     |
    E a resposta do cadastro deve conter somente o token e o tempo de expiração:
      | token           | token-jwt-seguro |
      | tempoExpiracao | 900000           |
    E a senha do usuário deve ser codificada antes de salvar
    E deve associar a role "ROLE_USER" ao usuário
    E as credenciais devem ser autenticadas antes de gerar o token

  Cenário: Criar conta de usuário — E-mail duplicado após normalização
    Dado os dados para criar a conta de usuário:
      | nome  | Ana                 |
      | email | " ANA@EXAMPLE.COM " |
      | senha | 12345678            |
    E existe outro usuário com e-mail normalizado "ana@example.com"

    Quando crio a conta de usuário

    Então a operação de usuário deve ser rejeitada com "EMAIL_DUPLICADO" sem salvar

  Cenário: Criar conta de usuário — Nome obrigatório ausente
    Dado os dados para criar a conta de usuário:
      | nome  |                 |
      | email | ana@example.com |
      | senha | 12345678        |

    Quando crio a conta de usuário

    Então a operação de usuário deve ser rejeitada com "NOME_OBRIGATORIO" sem salvar

  Cenário: Criar conta de usuário — E-mail obrigatório ausente
    Dado os dados para criar a conta de usuário:
      | nome  | Ana      |
      | email |          |
      | senha | 12345678 |

    Quando crio a conta de usuário

    Então a operação de usuário deve ser rejeitada com "EMAIL_OBRIGATORIO" sem salvar

  Cenário: Criar conta de usuário — Senha menor que oito caracteres
    Dado os dados para criar a conta de usuário:
      | nome  | Ana             |
      | email | ana@example.com |
      | senha | 1234567         |

    Quando crio a conta de usuário

    Então a operação de usuário deve ser rejeitada com "SENHA_CURTA" sem salvar

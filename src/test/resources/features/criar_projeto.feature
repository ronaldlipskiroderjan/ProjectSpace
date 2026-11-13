# language: pt
Funcionalidade: Criar projeto

  Cenário: Criar projeto — Criação válida
    Dado os dados para criar o projeto:
      | titulo     | Projeto   |
      | prioridade | média    |
      | status     | planejado |

    Quando crio o projeto

    Então deve salvar o projeto com os valores:
      | titulo     | Projeto                              |
      | prioridade | média                               |
      | status     | planejado                            |
      | usuarioId  | 00000000-0000-0000-0000-000000000001 |
    E o método de criação do projeto não deve retornar conteúdo

  Cenário: Criar projeto — Título vazio
    Dado os dados para criar o projeto:
      | titulo     |           |
      | prioridade | média    |
      | status     | planejado |

    Quando crio o projeto

    Então a criação do projeto deve ser rejeitada com "TITULO_OBRIGATORIO" sem salvar

  Cenário: Criar projeto — Usuário não autenticado
    Dado os dados para criar o projeto:
      | titulo     | Projeto   |
      | prioridade | média    |
      | status     | planejado |
    E o usuário do projeto não está autenticado

    Quando crio o projeto

    Então a criação do projeto deve ser rejeitada com "NAO_AUTENTICADO" sem salvar

  Cenário: Criar projeto — Prioridade inválida
    Dado os dados para criar o projeto:
      | titulo     | Projeto   |
      | prioridade | urgente   |
      | status     | planejado |

    Quando crio o projeto

    Então a criação do projeto deve ser rejeitada com "PRIORIDADE_INVALIDA" sem salvar

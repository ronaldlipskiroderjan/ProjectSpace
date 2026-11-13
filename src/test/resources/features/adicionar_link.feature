# language: pt
Funcionalidade: Adicionar link de projeto

  Cenário: Adicionar link de projeto — Adicionar link ao projeto
    Dado os dados para adicionar o link ao projeto:
      | projetoId | 10000000-0000-0000-0000-000000000001 |
      | url       | https://example.com/docs             |
    E existe para o link um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"

    Quando adiciono o link ao projeto

    Então deve salvar o link com os valores:
      | projetoId | 10000000-0000-0000-0000-000000000001 |
      | url       | https://example.com/docs             |
    E o método de criação do link não deve retornar conteúdo

  Cenário: Adicionar link de projeto — Preservar endereço sem validar o formato
    Dado os dados para adicionar o link ao projeto:
      | projetoId | 10000000-0000-0000-0000-000000000001 |
      | url       | documentação                       |
    E existe para o link um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"

    Quando adiciono o link ao projeto

    Então deve salvar o link com os valores:
      | projetoId | 10000000-0000-0000-0000-000000000001 |
      | url       | documentação                       |
    E o método de criação do link não deve retornar conteúdo

  Cenário: Adicionar link de projeto — Projeto inexistente
    Dado os dados para adicionar o link ao projeto:
      | projetoId | 10000000-0000-0000-0000-000000000001 |
      | url       | https://example.com                  |

    Quando adiciono o link ao projeto

    Então a operação de link deve ser rejeitada com "PROJETO_NAO_ENCONTRADO" sem salvar

  Cenário: Adicionar link de projeto — Projeto de outro usuário
    Dado os dados para adicionar o link ao projeto:
      | projetoId | 10000000-0000-0000-0000-000000000001 |
      | url       | https://example.com                  |
    E existe para o link um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000002"

    Quando adiciono o link ao projeto

    Então a operação de link deve ser rejeitada com "ACESSO_NEGADO" sem salvar

# language: pt
Funcionalidade: Visualizar links

  Cenário: Visualizar links — Listar links de projeto próprio
    Dado existe para visualização um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000001"
    E existem para visualização os links do projeto:
      | linkId                               | projetoId                           | url                      |
      | 40000000-0000-0000-0000-000000000001 | 10000000-0000-0000-0000-000000000001 | https://example.com/docs |
      | 40000000-0000-0000-0000-000000000002 | 10000000-0000-0000-0000-000000000001 | documentação             |

    Quando visualizo os links do projeto "10000000-0000-0000-0000-000000000001"

    Então devem ser retornados os links:
      | linkId                               | projetoId                           | url                      |
      | 40000000-0000-0000-0000-000000000001 | 10000000-0000-0000-0000-000000000001 | https://example.com/docs |
      | 40000000-0000-0000-0000-000000000002 | 10000000-0000-0000-0000-000000000001 | documentação             |
    E a visualização não deve alterar dados

  Cenário: Visualizar links — Projeto de outro usuário
    Dado existe para visualização um projeto com id "10000000-0000-0000-0000-000000000001" e usuário "00000000-0000-0000-0000-000000000002"

    Quando visualizo os links do projeto "10000000-0000-0000-0000-000000000001"

    Então a visualização deve ser rejeitada com "ACESSO_NEGADO"
    E a visualização não deve alterar dados

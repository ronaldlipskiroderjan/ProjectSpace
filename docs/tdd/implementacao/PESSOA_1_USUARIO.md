# Pessoa 1 — Cadastrar e editar usuário

## Responsabilidade

Esta pessoa implementará, nesta ordem:

1. `criar_usuario.feature` em `AuthService.criarUsuario`.
2. `editar_usuario.feature` em `UsuarioService.editarUsuario`.
3. `visualizar_perfil.feature` em `UsuarioService.buscarPerfil`.

Arquivos principais:

- `service/AuthService.java`
- `service/UsuarioService.java`
- `config/TokenProvider.java`
- `database/model/UsuarioEntity.java`
- `database/repository/UsuarioRepository.java`
- DTOs de usuário e token

Não altere os arquivos `.feature` ou os Steps apenas para fazer os testes passarem. Eles são o contrato da implementação.

## 1. Cadastrar usuário

Implemente `AuthService.criarUsuario(UsuarioRequestDTO)` seguindo esta sequência:

1. Rejeitar nome nulo ou em branco com `NOME_OBRIGATORIO`.
2. Rejeitar e-mail nulo ou em branco com `EMAIL_OBRIGATORIO`.
3. Rejeitar senha com menos de oito caracteres com `SENHA_CURTA`.
4. Normalizar o e-mail com `trim()` e `toLowerCase(Locale.ROOT)`.
5. Consultar `usuarioRepository.findByEmailIgnoreCase(emailNormalizado)`.
6. Se já existir usuário, lançar erro com `EMAIL_DUPLICADO`.
7. Codificar a senha usando `passwordEncoder.encode(request.senha())`.
8. Criar `UsuarioEntity` com `usuarioId` nulo, nome, e-mail normalizado e `senhaHash`.
9. Salvar a entidade no `UsuarioRepository`.
10. Criar um `Authentication` cujo `principal` seja exatamente a entidade salva.
11. Gerar o token com `tokenProvider.gerarToken(authentication)`.
12. Retornar `TokenResponseDTO` com o token e `tokenProvider.getExpirationTime()`.

Para a integração real com o provider, `UsuarioEntity` deverá implementar `UserDetails`. No `TokenProvider`, use `getUsuarioId()` como identificador do usuário. Não coloque senha ou hash no DTO de resposta.

## 2. Editar usuário

Implemente `UsuarioService.editarUsuario(UUID usuarioAutenticadoId, UUID usuarioId, UsuarioRequestDTO request)`:

1. Comparar o usuário autenticado com o usuário solicitado.
2. Se os IDs forem diferentes, lançar `ACESSO_NEGADO`.
3. Buscar a conta com `usuarioRepository.findById(usuarioId)`.
4. Validar o formato do novo e-mail; para formato inválido, lançar `EMAIL_INVALIDO`.
5. Normalizar o e-mail com `trim()` e letras minúsculas.
6. Consultar `findByEmailIgnoreCase`.
7. Se o e-mail pertencer a outro `usuarioId`, lançar `EMAIL_DUPLICADO`.
8. Atualizar somente nome e e-mail. A edição não deve apagar nem substituir `senhaHash`.
9. Salvar a mesma entidade recuperada.
10. Retornar `UsuarioResponseDTO` somente com `usuarioId`, `nome` e `email`.

## 3. Visualizar perfil

Implemente `UsuarioService.buscarPerfil(UUID usuarioAutenticadoId)`:

1. Buscar o usuário autenticado com `usuarioRepository.findById`.
2. Se ele não existir, lançar `USUARIO_NAO_ENCONTRADO`.
3. Retornar `UsuarioResponseDTO` com `usuarioId`, `nome` e `email`.
4. Não retornar senha ou hash e não chamar nenhum método de escrita.

## Erros de negócio

Nesta etapa, pode ser usada uma exceção de runtime com a mensagem exata esperada, por exemplo:

```java
throw new IllegalArgumentException("EMAIL_DUPLICADO");
```

Se a equipe criar uma exceção de negócio própria, todos devem usar a mesma classe e preservar exatamente as mensagens dos cenários.

## Critério de conclusão

- Os onze cenários de usuário e visualização de perfil passam.
- A senha é codificada antes do `save`.
- O token é gerado para a entidade efetivamente salva.
- Nenhuma operação inválida chama `save` ou gera token.
- Os DTOs de resposta não expõem `senha` nem `senhaHash`.
- A suíte completa continua compilando.

Execute:

```bash
docker compose run --rm cucumber
```

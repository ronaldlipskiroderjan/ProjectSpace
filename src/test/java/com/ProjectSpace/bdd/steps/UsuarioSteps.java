package com.ProjectSpace.bdd.steps;

import com.ProjectSpace.config.TokenProvider;
import com.ProjectSpace.database.model.RoleEntity;
import com.ProjectSpace.database.repository.RoleRepository;
import com.ProjectSpace.dto.request.UsuarioRequestDTO;
import com.ProjectSpace.dto.response.TokenResponseDTO;
import com.ProjectSpace.dto.response.UsuarioResponseDTO;
import com.ProjectSpace.database.model.UsuarioEntity;
import com.ProjectSpace.database.repository.UsuarioRepository;
import com.ProjectSpace.enums.RoleTypeEnum;
import com.ProjectSpace.service.AuthService;
import com.ProjectSpace.service.UsuarioService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsuarioSteps {

    private static final String USUARIO_AUTENTICADO_EMAIL = "ana@example.com";
    private static final long EXPIRACAO_TOKEN = 900000L;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication autenticacaoValidada;

    @InjectMocks
    private UsuarioService usuarioService;

    @InjectMocks
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private Map<String, String> dados;
    private TokenResponseDTO respostaCadastro;
    private UsuarioResponseDTO respostaEdicao;
    private Throwable erro;

    @Before
    public void prepararCenario() {
        MockitoAnnotations.openMocks(this);
        dados = new HashMap<>();
        respostaCadastro = null;
        respostaEdicao = null;
        erro = null;
        when(passwordEncoder.encode(any(String.class))).thenReturn("hash-seguro");
        when(usuarioRepository.save(any(UsuarioEntity.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
        when(roleRepository.findByNome(RoleTypeEnum.ROLE_USER.name()))
                .thenReturn(Optional.of(RoleEntity.builder()
                        .nome(RoleTypeEnum.ROLE_USER.name())
                        .build()));
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(autenticacaoValidada);
        when(tokenProvider.gerarToken(any(Authentication.class))).thenReturn("token-jwt-seguro");
        ReflectionTestUtils.setField(authService, "expirationTime", EXPIRACAO_TOKEN);
    }

    @Dado("os dados para criar a conta de usuário:")
    @Dado("os dados para editar a conta de usuário:")
    public void informarDadosDoUsuario(DataTable tabela) {
        dados = valoresDa(tabela);
    }

    @E("existe um usuário autenticado com e-mail {string} e id {string}")
    public void existeUsuarioAutenticado(String email, String usuarioId) {
        UsuarioEntity usuario = new UsuarioEntity();
        ReflectionTestUtils.setField(usuario, "usuarioId", uuid(usuarioId));
        ReflectionTestUtils.setField(usuario, "email", email);
        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(usuario));
    }

    @E("existe outro usuário com e-mail normalizado {string}")
    public void existeUsuarioComEmail(String email) {
        UsuarioEntity usuario = new UsuarioEntity();
        ReflectionTestUtils.setField(usuario, "email", email);
        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailIgnoreCase(email)).thenReturn(true);
    }

    @E("existe outro usuário com e-mail normalizado {string} e id {string}")
    public void existeOutroUsuarioComEmail(String email, String usuarioId) {
        UsuarioEntity usuario = new UsuarioEntity();
        ReflectionTestUtils.setField(usuario, "usuarioId", uuid(usuarioId));
        ReflectionTestUtils.setField(usuario, "email", email);
        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(usuario));
    }

    @Quando("crio a conta de usuário")
    public void criarConta() {
        erro = catchThrowable(() -> {
            UsuarioRequestDTO request = request();
            respostaCadastro = authService.criarUsuario(request);
        });
    }

    @Quando("edito a conta de usuário")
    public void editarConta() {
        erro = catchThrowable(() -> {
            UsuarioRequestDTO request = request();
            respostaEdicao = usuarioService.editarUsuario(
                    USUARIO_AUTENTICADO_EMAIL, request);
        });
    }

    @Entao("deve salvar o usuário com os valores:")
    public void verificarUsuarioSalvo(DataTable tabela) {
        Map<String, String> esperado = valoresDa(tabela);
        ArgumentCaptor<UsuarioEntity> captor = ArgumentCaptor.forClass(UsuarioEntity.class);

        assertThat(erro).isNull();
        verify(usuarioRepository).save(captor.capture());
        assertEntityContem(captor.getValue(), esperado);
    }

    @E("a resposta do usuário deve conter:")
    public void verificarRespostaDoUsuario(DataTable tabela) {
        assertThat(respostaEdicao).isNotNull();
        assertResponseContem(respostaEdicao, valoresDa(tabela));
    }

    @E("a resposta do cadastro deve conter somente o token e o tempo de expiração:")
    public void verificarRespostaDoCadastro(DataTable tabela) {
        assertThat(respostaCadastro).isNotNull();
        Map<String, Object> valores = objectMapper.convertValue(
                respostaCadastro, new TypeReference<>() { });
        valoresDa(tabela).forEach((campo, valor) ->
                assertThat(String.valueOf(valores.get(campo))).isEqualTo(valor));
        assertThat(valores).containsOnlyKeys("token", "tempoExpiracao");
    }

    @E("a senha do usuário deve ser codificada antes de salvar")
    public void verificarSenhaCodificada() {
        verify(passwordEncoder).encode(dados.get("senha"));
    }

    @E("deve associar a role {string} ao usuário")
    public void verificarRoleDoUsuario(String role) {
        ArgumentCaptor<UsuarioEntity> usuarioSalvo = ArgumentCaptor.forClass(UsuarioEntity.class);
        verify(usuarioRepository).save(usuarioSalvo.capture());
        assertThat(usuarioSalvo.getValue().getRoles())
                .extracting(RoleEntity::getNome)
                .containsExactly(role);
    }

    @E("as credenciais devem ser autenticadas antes de gerar o token")
    public void verificarAutenticacaoAntesDoToken() {
        ArgumentCaptor<Authentication> credenciais =
                ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(credenciais.capture());
        assertThat(credenciais.getValue().getPrincipal()).isEqualTo(dados.get("email"));
        assertThat(credenciais.getValue().getCredentials()).isEqualTo(dados.get("senha"));
        verify(tokenProvider).gerarToken(autenticacaoValidada);
    }

    @Entao("a operação de usuário deve ser rejeitada com {string} sem salvar")
    public void verificarOperacaoRejeitada(String mensagem) {
        assertThat(erro).isNotNull().hasMessage(mensagem);
        assertThat(respostaCadastro).isNull();
        assertThat(respostaEdicao).isNull();
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
        verify(authenticationManager, never()).authenticate(any(Authentication.class));
        verify(tokenProvider, never()).gerarToken(any(Authentication.class));
    }

    private UsuarioRequestDTO request() {
        return new UsuarioRequestDTO(
                dados.get("nome"),
                dados.get("email"),
                dados.get("senha"));
    }

    private void assertResponseContem(Object resposta, Map<String, String> esperado) {
        Map<String, Object> valores = objectMapper.convertValue(resposta, new TypeReference<>() { });
        esperado.forEach((campo, valor) ->
                assertThat(String.valueOf(valores.get(campo))).isEqualTo(valor));
        assertThat(valores).doesNotContainKeys("senha", "senhaHash");
    }

    private void assertEntityContem(UsuarioEntity usuario, Map<String, String> esperado) {
        esperado.forEach((campo, valor) -> {
            Object valorEsperado = campo.endsWith("Id") ? UUID.fromString(valor) : valor;
            assertThat(usuario).extracting(campo).isEqualTo(valorEsperado);
        });
        if (!esperado.containsKey("usuarioId")) {
            assertThat(usuario.getUsuarioId()).isNull();
        }
    }

    private Map<String, String> valoresDa(DataTable tabela) {
        Map<String, String> valores = new HashMap<>();
        tabela.asMap(String.class, String.class)
                .forEach((chave, valor) -> valores.put(chave, semAspas(valor)));
        return valores;
    }

    private String semAspas(String valor) {
        if (valor != null && valor.length() >= 2 && valor.startsWith("\"") && valor.endsWith("\"")) {
            return valor.substring(1, valor.length() - 1);
        }
        return valor;
    }

    private UUID uuid(String valor) {
        return UUID.fromString(valor);
    }
}

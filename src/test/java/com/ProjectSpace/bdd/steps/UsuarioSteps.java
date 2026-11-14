package com.ProjectSpace.bdd.steps;

import com.ProjectSpace.config.TokenProvider;
import com.ProjectSpace.dto.request.UsuarioRequestDTO;
import com.ProjectSpace.dto.response.TokenResponseDTO;
import com.ProjectSpace.dto.response.UsuarioResponseDTO;
import com.ProjectSpace.database.model.UsuarioEntity;
import com.ProjectSpace.database.repository.UsuarioRepository;
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

    private static final UUID USUARIO_AUTENTICADO_ID =
            UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

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
        when(tokenProvider.gerarToken(any(Authentication.class))).thenReturn("token-jwt-seguro");
        when(tokenProvider.getExpirationTime()).thenReturn(3600L);
    }

    @Dado("os dados para criar a conta de usuário:")
    @Dado("os dados para editar a conta de usuário:")
    public void informarDadosDoUsuario(DataTable tabela) {
        dados = valoresDa(tabela);
    }

    @E("existe um usuário com id {string}")
    public void existeUsuario(String usuarioId) {
        UsuarioEntity usuario = new UsuarioEntity();
        ReflectionTestUtils.setField(usuario, "usuarioId", uuid(usuarioId));
        when(usuarioRepository.findById(uuid(usuarioId))).thenReturn(Optional.of(usuario));
    }

    @E("existe outro usuário com e-mail normalizado {string} e id {string}")
    public void existeUsuarioComEmail(String email, String usuarioId) {
        UsuarioEntity usuario = new UsuarioEntity();
        ReflectionTestUtils.setField(usuario, "usuarioId", uuid(usuarioId));
        ReflectionTestUtils.setField(usuario, "email", email);
        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(usuario));
    }

    @Quando("crio a conta de usuário")
    public void criarConta() {
        UsuarioRequestDTO request = request();
        erro = catchThrowable(() -> respostaCadastro = authService.criarUsuario(request));
    }

    @Quando("edito a conta de usuário")
    public void editarConta() {
        UsuarioRequestDTO request = request();
        UUID usuarioId = uuid(dados.get("usuarioId"));
        erro = catchThrowable(() -> respostaEdicao =
                usuarioService.editarUsuario(USUARIO_AUTENTICADO_ID, usuarioId, request));
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

    @E("o token deve ser gerado para o usuário salvo")
    public void verificarTokenGeradoParaUsuarioSalvo() {
        ArgumentCaptor<UsuarioEntity> usuarioSalvo = ArgumentCaptor.forClass(UsuarioEntity.class);
        ArgumentCaptor<Authentication> autenticacaoDoToken =
                ArgumentCaptor.forClass(Authentication.class);
        verify(usuarioRepository).save(usuarioSalvo.capture());
        verify(tokenProvider).gerarToken(autenticacaoDoToken.capture());
        assertThat(autenticacaoDoToken.getValue().getPrincipal())
                .isSameAs(usuarioSalvo.getValue());
    }

    @Entao("a operação de usuário deve ser rejeitada com {string} sem salvar")
    public void verificarOperacaoRejeitada(String mensagem) {
        assertThat(erro).isNotNull().hasMessage(mensagem);
        assertThat(respostaCadastro).isNull();
        assertThat(respostaEdicao).isNull();
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
        verify(tokenProvider, never()).gerarToken(any(Authentication.class));
    }

    private UsuarioRequestDTO request() {
        Map<String, String> camposDoRequest = new HashMap<>(dados);
        camposDoRequest.remove("usuarioId");
        return objectMapper.convertValue(camposDoRequest, UsuarioRequestDTO.class);
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
        if (!dados.containsKey("usuarioId")) {
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

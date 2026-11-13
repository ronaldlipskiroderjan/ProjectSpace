package com.ProjectSpace.bdd.steps;

import com.ProjectSpace.dto.request.ProjetoRequestDTO;
import com.ProjectSpace.entity.ProjetoEntity;
import com.ProjectSpace.repository.ProjetoRepository;
import com.ProjectSpace.service.ProjetoService;
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

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjetoSteps {

    private static final UUID USUARIO_AUTENTICADO_ID =
            UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private ProjetoService projetoService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private Map<String, String> dados;
    private UUID usuarioAutenticadoId;
    private Throwable erro;

    @Before
    public void prepararCenario() {
        MockitoAnnotations.openMocks(this);
        usuarioAutenticadoId = USUARIO_AUTENTICADO_ID;
        erro = null;
        when(projetoRepository.save(any(ProjetoEntity.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
    }

    @Dado("os dados para criar o projeto:")
    public void informarDadosDoProjeto(DataTable tabela) {
        dados = tabela.asMap(String.class, String.class);
    }

    @E("o usuário do projeto não está autenticado")
    public void usuarioNaoAutenticado() {
        usuarioAutenticadoId = null;
    }

    @Quando("crio o projeto")
    public void criarProjeto() {
        ProjetoRequestDTO request = objectMapper.convertValue(dados, ProjetoRequestDTO.class);
        erro = catchThrowable(() -> projetoService.criarProjeto(usuarioAutenticadoId, request));
    }

    @Entao("deve salvar o projeto com os valores:")
    public void verificarProjetoSalvo(DataTable tabela) {
        Map<String, String> esperado = tabela.asMap(String.class, String.class);
        ArgumentCaptor<ProjetoEntity> captor = ArgumentCaptor.forClass(ProjetoEntity.class);

        assertThat(erro).isNull();
        verify(projetoRepository).save(captor.capture());
        assertEntityContem(captor.getValue(), esperado);
        assertThat(captor.getValue().getProjetoId()).isNull();
    }

    @E("o método de criação do projeto não deve retornar conteúdo")
    public void verificarRetornoVoid() throws NoSuchMethodException {
        assertThat(ProjetoService.class
                .getDeclaredMethod("criarProjeto", UUID.class, ProjetoRequestDTO.class)
                .getReturnType()).isEqualTo(void.class);
    }

    @Entao("a criação do projeto deve ser rejeitada com {string} sem salvar")
    public void verificarCriacaoRejeitada(String mensagem) {
        assertThat(erro).isNotNull().hasMessage(mensagem);
        verify(projetoRepository, never()).save(any(ProjetoEntity.class));
    }

    private void assertEntityContem(ProjetoEntity projeto, Map<String, String> esperado) {
        esperado.forEach((campo, valor) -> {
            Object valorEsperado = campo.equals("usuarioId") ? UUID.fromString(valor) : valor;
            assertThat(projeto).extracting(campo).isEqualTo(valorEsperado);
        });
    }
}

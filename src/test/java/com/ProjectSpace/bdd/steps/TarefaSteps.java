package com.ProjectSpace.bdd.steps;

import com.ProjectSpace.dto.request.TarefaRequestDTO;
import com.ProjectSpace.dto.response.TarefaResponseDTO;
import com.ProjectSpace.entity.ProjetoEntity;
import com.ProjectSpace.entity.TarefaEntity;
import com.ProjectSpace.repository.ProjetoRepository;
import com.ProjectSpace.repository.TarefaRepository;
import com.ProjectSpace.service.TarefaService;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TarefaSteps {

    private static final UUID USUARIO_AUTENTICADO_ID =
            UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private Map<String, String> dados;
    private TarefaResponseDTO resposta;
    private Throwable erro;

    @Before
    public void prepararCenario() {
        MockitoAnnotations.openMocks(this);
        dados = new HashMap<>();
        resposta = null;
        erro = null;
        when(tarefaRepository.save(any(TarefaEntity.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
    }

    @Dado("os dados para adicionar a tarefa:")
    @Dado("os dados para editar a tarefa:")
    @Dado("os dados para excluir a tarefa:")
    public void informarDadosDaTarefa(DataTable tabela) {
        dados = new HashMap<>(tabela.asMap(String.class, String.class));
    }

    @E("existe para a tarefa um projeto com id {string} e usuário {string}")
    public void existeProjeto(String projetoId, String usuarioId) {
        ProjetoEntity projeto = mock(ProjetoEntity.class);
        when(projeto.getProjetoId()).thenReturn(uuid(projetoId));
        when(projeto.getUsuarioId()).thenReturn(uuid(usuarioId));
        when(projetoRepository.findById(uuid(projetoId))).thenReturn(Optional.of(projeto));
    }

    @E("existe uma tarefa com id {string} no projeto {string}")
    public void existeTarefa(String tarefaId, String projetoId) {
        TarefaEntity tarefa = new TarefaEntity();
        ReflectionTestUtils.setField(tarefa, "tarefaId", uuid(tarefaId));
        ReflectionTestUtils.setField(tarefa, "projetoId", uuid(projetoId));
        when(tarefaRepository.findById(uuid(tarefaId))).thenReturn(Optional.of(tarefa));
    }

    @Quando("adiciono a tarefa")
    public void adicionarTarefa() {
        TarefaRequestDTO request = requestSemId();
        erro = catchThrowable(() -> tarefaService.adicionarTarefa(USUARIO_AUTENTICADO_ID, request));
    }

    @Quando("edito a tarefa")
    public void editarTarefa() {
        UUID tarefaId = uuid(dados.get("tarefaId"));
        TarefaRequestDTO request = requestSemId();
        erro = catchThrowable(() -> resposta =
                tarefaService.editarTarefa(USUARIO_AUTENTICADO_ID, tarefaId, request));
    }

    @Quando("excluo a tarefa")
    public void excluirTarefa() {
        UUID tarefaId = uuid(dados.get("tarefaId"));
        erro = catchThrowable(() -> tarefaService.excluirTarefa(USUARIO_AUTENTICADO_ID, tarefaId));
    }

    @Quando("excluo a tarefa duas vezes")
    public void excluirTarefaDuasVezes() {
        UUID tarefaId = uuid(dados.get("tarefaId"));
        tarefaService.excluirTarefa(USUARIO_AUTENTICADO_ID, tarefaId);
        clearInvocations(tarefaRepository);
        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.empty());
        erro = catchThrowable(() -> tarefaService.excluirTarefa(USUARIO_AUTENTICADO_ID, tarefaId));
    }

    @Entao("deve salvar a tarefa com os valores:")
    public void verificarTarefaSalva(DataTable tabela) {
        Map<String, String> esperado = tabela.asMap(String.class, String.class);
        ArgumentCaptor<TarefaEntity> captor = ArgumentCaptor.forClass(TarefaEntity.class);

        assertThat(erro).isNull();
        verify(tarefaRepository).save(captor.capture());
        assertEntityContem(captor.getValue(), esperado);
        if (!esperado.containsKey("tarefaId")) {
            assertThat(captor.getValue().getTarefaId()).isNull();
        }
    }

    @E("a resposta da tarefa deve conter:")
    public void verificarRespostaDaTarefa(DataTable tabela) {
        assertThat(resposta).isNotNull();
        assertResponseContem(tabela.asMap(String.class, String.class));
    }

    @E("o método de criação da tarefa não deve retornar conteúdo")
    public void verificarRetornoVoid() throws NoSuchMethodException {
        assertThat(TarefaService.class
                .getDeclaredMethod("adicionarTarefa", UUID.class, TarefaRequestDTO.class)
                .getReturnType()).isEqualTo(void.class);
    }

    @Entao("a operação de tarefa deve ser rejeitada com {string} sem salvar")
    public void verificarOperacaoRejeitada(String mensagem) {
        assertThat(erro).isNotNull().hasMessage(mensagem);
        assertThat(resposta).isNull();
        verify(tarefaRepository, never()).save(any(TarefaEntity.class));
        verify(tarefaRepository, never()).deleteById(any(UUID.class));
    }

    @Entao("deve remover somente a tarefa {string}")
    public void verificarTarefaRemovida(String tarefaId) {
        assertThat(erro).isNull();
        verify(tarefaRepository).deleteById(uuid(tarefaId));
    }

    private TarefaRequestDTO requestSemId() {
        Map<String, String> camposDoRequest = new HashMap<>(dados);
        camposDoRequest.remove("tarefaId");
        return objectMapper.convertValue(camposDoRequest, TarefaRequestDTO.class);
    }

    private void assertResponseContem(Map<String, String> esperado) {
        Map<String, Object> valores = objectMapper.convertValue(resposta, new TypeReference<>() { });
        esperado.forEach((campo, valor) ->
                assertThat(String.valueOf(valores.get(campo))).isEqualTo(valor));
    }

    private void assertEntityContem(TarefaEntity tarefa, Map<String, String> esperado) {
        esperado.forEach((campo, valor) -> {
            Object valorEsperado = campo.endsWith("Id") ? UUID.fromString(valor) : valor;
            assertThat(tarefa).extracting(campo).isEqualTo(valorEsperado);
        });
    }

    private UUID uuid(String valor) {
        return UUID.fromString(valor);
    }
}

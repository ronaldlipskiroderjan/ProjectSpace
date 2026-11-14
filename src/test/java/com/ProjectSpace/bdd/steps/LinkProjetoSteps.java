package com.ProjectSpace.bdd.steps;

import com.ProjectSpace.dto.request.LinkRequestDTO;
import com.ProjectSpace.database.model.LinkEntity;
import com.ProjectSpace.database.model.ProjetoEntity;
import com.ProjectSpace.database.repository.LinkRepository;
import com.ProjectSpace.database.repository.ProjetoRepository;
import com.ProjectSpace.service.LinkService;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LinkProjetoSteps {

    private static final UUID USUARIO_AUTENTICADO_ID =
            UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private LinkService linkService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private Map<String, String> dados;
    private Throwable erro;

    @Before
    public void prepararCenario() {
        MockitoAnnotations.openMocks(this);
        erro = null;
        when(linkRepository.save(any(LinkEntity.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
    }

    @Dado("os dados para adicionar o link ao projeto:")
    public void informarDadosDoLink(DataTable tabela) {
        dados = tabela.asMap(String.class, String.class);
    }

    @E("existe para o link um projeto com id {string} e usuário {string}")
    public void existeProjeto(String projetoId, String usuarioId) {
        ProjetoEntity projeto = mock(ProjetoEntity.class);
        when(projeto.getProjetoId()).thenReturn(uuid(projetoId));
        when(projeto.getUsuarioId()).thenReturn(uuid(usuarioId));
        when(projetoRepository.findById(uuid(projetoId))).thenReturn(Optional.of(projeto));
    }

    @Quando("adiciono o link ao projeto")
    public void adicionarLink() {
        LinkRequestDTO request = objectMapper.convertValue(dados, LinkRequestDTO.class);
        erro = catchThrowable(() -> linkService.adicionarLink(USUARIO_AUTENTICADO_ID, request));
    }

    @Entao("deve salvar o link com os valores:")
    public void verificarLinkSalvo(DataTable tabela) {
        Map<String, String> esperado = tabela.asMap(String.class, String.class);
        ArgumentCaptor<LinkEntity> captor = ArgumentCaptor.forClass(LinkEntity.class);

        assertThat(erro).isNull();
        verify(linkRepository).save(captor.capture());
        assertEntityContem(captor.getValue(), esperado);
        assertThat(captor.getValue().getLinkId()).isNull();
    }

    @E("o método de criação do link não deve retornar conteúdo")
    public void verificarRetornoVoid() throws NoSuchMethodException {
        assertThat(LinkService.class
                .getDeclaredMethod("adicionarLink", UUID.class, LinkRequestDTO.class)
                .getReturnType()).isEqualTo(void.class);
    }

    @Entao("a operação de link deve ser rejeitada com {string} sem salvar")
    public void verificarOperacaoRejeitada(String mensagem) {
        assertThat(erro).isNotNull().hasMessage(mensagem);
        verify(linkRepository, never()).save(any(LinkEntity.class));
    }

    private void assertEntityContem(LinkEntity link, Map<String, String> esperado) {
        esperado.forEach((campo, valor) -> {
            Object valorEsperado = campo.equals("projetoId") ? UUID.fromString(valor) : valor;
            assertThat(link).extracting(campo).isEqualTo(valorEsperado);
        });
    }

    private UUID uuid(String valor) {
        return UUID.fromString(valor);
    }
}

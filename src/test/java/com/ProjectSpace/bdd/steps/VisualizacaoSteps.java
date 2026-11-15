package com.ProjectSpace.bdd.steps;

import com.ProjectSpace.database.model.LinkEntity;
import com.ProjectSpace.database.model.ProjetoEntity;
import com.ProjectSpace.database.model.TagEntity;
import com.ProjectSpace.database.model.TarefaEntity;
import com.ProjectSpace.database.model.UsuarioEntity;
import com.ProjectSpace.database.repository.LinkRepository;
import com.ProjectSpace.database.repository.ProjetoRepository;
import com.ProjectSpace.database.repository.TagRepository;
import com.ProjectSpace.database.repository.TarefaRepository;
import com.ProjectSpace.database.repository.UsuarioRepository;
import com.ProjectSpace.dto.response.UsuarioResponseDTO;
import com.ProjectSpace.service.LinkService;
import com.ProjectSpace.service.ProjetoService;
import com.ProjectSpace.service.TagService;
import com.ProjectSpace.service.TarefaService;
import com.ProjectSpace.service.UsuarioService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VisualizacaoSteps {

    private static final UUID USUARIO_AUTENTICADO_ID =
            UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final String USUARIO_AUTENTICADO_EMAIL = "ana@example.com";

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @InjectMocks
    private ProjetoService projetoService;

    @InjectMocks
    private TarefaService tarefaService;

    @InjectMocks
    private LinkService linkService;

    @InjectMocks
    private TagService tagService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private UUID usuarioAutenticadoId;
    private String emailUsuarioAutenticado;
    private UsuarioResponseDTO perfil;
    private List<?> resultados;
    private Throwable erro;
    private TipoConsulta tipoConsulta;
    private ProjetoEntity projetoConsultado;

    @Before
    public void prepararCenario() {
        MockitoAnnotations.openMocks(this);
        usuarioAutenticadoId = USUARIO_AUTENTICADO_ID;
        emailUsuarioAutenticado = USUARIO_AUTENTICADO_EMAIL;
        perfil = null;
        resultados = null;
        erro = null;
        tipoConsulta = null;
        projetoConsultado = null;
    }

    @Dado("existe para visualização um usuário com os valores:")
    public void existeUsuario(DataTable tabela) {
        Map<String, String> dados = tabela.asMap(String.class, String.class);
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUsuarioId(uuid(dados.get("usuarioId")));
        usuario.setNome(dados.get("nome"));
        usuario.setEmail(dados.get("email"));
        emailUsuarioAutenticado = usuario.getEmail();
        when(usuarioRepository.findByEmailIgnoreCase(usuario.getEmail()))
                .thenReturn(java.util.Optional.of(usuario));
    }

    @Dado("não há usuário autenticado para visualização")
    public void usuarioNaoAutenticado() {
        usuarioAutenticadoId = null;
        emailUsuarioAutenticado = null;
    }

    @Dado("existem para visualização os projetos:")
    public void existemProjetos(DataTable tabela) {
        List<ProjetoEntity> projetos = tabela.asMaps(String.class, String.class).stream()
                .map(this::novoProjeto)
                .toList();
        when(projetoRepository.findAllByUsuario_UsuarioId(USUARIO_AUTENTICADO_ID)).thenReturn(projetos);
    }

    @Dado("existe para visualização um projeto com id {string} e usuário {string}")
    public void existeProjeto(String projetoId, String usuarioId) {
        projetoConsultado = mock(ProjetoEntity.class);
        UsuarioEntity usuario = mock(UsuarioEntity.class);
        when(projetoConsultado.getProjetoId()).thenReturn(uuid(projetoId));
        when(projetoConsultado.getUsuario()).thenReturn(usuario);
        when(usuario.getUsuarioId()).thenReturn(uuid(usuarioId));
        when(projetoRepository.findById(uuid(projetoId)))
                .thenReturn(java.util.Optional.of(projetoConsultado));
    }

    @E("existem para visualização as tarefas do projeto:")
    public void existemTarefas(DataTable tabela) {
        List<TarefaEntity> tarefas = tabela.asMaps(String.class, String.class).stream()
                .map(this::novaTarefa)
                .toList();
        when(tarefaRepository.findAllByProjeto_ProjetoId(
                tarefas.getFirst().getProjeto().getProjetoId()))
                .thenReturn(tarefas);
    }

    @E("existem para visualização os links do projeto:")
    public void existemLinks(DataTable tabela) {
        List<LinkEntity> links = tabela.asMaps(String.class, String.class).stream()
                .map(this::novoLink)
                .toList();
        when(linkRepository.findAllByProjeto_ProjetoId(
                links.getFirst().getProjeto().getProjetoId()))
                .thenReturn(links);
    }

    @E("existem para visualização as tags do projeto:")
    public void existemTags(DataTable tabela) {
        Set<TagEntity> tags = new LinkedHashSet<>();
        tabela.asMaps(String.class, String.class).stream()
                .map(this::novaTag)
                .forEach(tags::add);
        when(projetoConsultado.getTags()).thenReturn(tags);
    }

    @Quando("visualizo meu perfil")
    public void visualizarPerfil() {
        tipoConsulta = TipoConsulta.PERFIL;
        erro = catchThrowable(() -> perfil = usuarioService.buscarPerfil(emailUsuarioAutenticado));
    }

    @Quando("visualizo meus projetos")
    public void visualizarProjetos() {
        tipoConsulta = TipoConsulta.PROJETOS;
        erro = catchThrowable(() -> resultados = projetoService.listarProjetos(usuarioAutenticadoId));
    }

    @Quando("visualizo as tarefas do projeto {string}")
    public void visualizarTarefas(String projetoId) {
        tipoConsulta = TipoConsulta.TAREFAS;
        erro = catchThrowable(() -> resultados =
                tarefaService.listarTarefas(usuarioAutenticadoId, uuid(projetoId)));
    }

    @Quando("visualizo os links do projeto {string}")
    public void visualizarLinks(String projetoId) {
        tipoConsulta = TipoConsulta.LINKS;
        erro = catchThrowable(() -> resultados =
                linkService.listarLinks(usuarioAutenticadoId, uuid(projetoId)));
    }

    @Quando("visualizo as tags do projeto {string}")
    public void visualizarTags(String projetoId) {
        tipoConsulta = TipoConsulta.TAGS;
        erro = catchThrowable(() -> resultados =
                tagService.listarTags(usuarioAutenticadoId, uuid(projetoId)));
    }

    @Entao("o perfil retornado deve conter:")
    public void verificarPerfil(DataTable tabela) {
        assertThat(erro).isNull();
        assertThat(perfil).isNotNull();
        assertObjetoContem(perfil, tabela.asMap(String.class, String.class));
    }

    @Entao("devem ser retornados os projetos:")
    @Entao("devem ser retornadas as tarefas:")
    @Entao("devem ser retornados os links:")
    @Entao("devem ser retornadas as tags:")
    public void verificarLista(DataTable tabela) {
        assertThat(erro).isNull();
        assertThat(resultados).isNotNull();

        List<Map<String, String>> esperados = tabela.asMaps(String.class, String.class);
        assertThat(resultados).hasSameSizeAs(esperados);

        for (int indice = 0; indice < esperados.size(); indice++) {
            assertObjetoContem(resultados.get(indice), esperados.get(indice));
        }
    }

    @Entao("a visualização deve ser rejeitada com {string}")
    public void verificarConsultaRejeitada(String mensagem) {
        assertThat(erro).isNotNull().hasMessage(mensagem);
        assertThat(perfil).isNull();
        assertThat(resultados).isNull();

        if (tipoConsulta == TipoConsulta.PROJETOS) {
            verify(projetoRepository, never()).findAllByUsuario_UsuarioId(any());
        } else if (tipoConsulta == TipoConsulta.TAREFAS) {
            verify(tarefaRepository, never()).findAllByProjeto_ProjetoId(any());
        } else if (tipoConsulta == TipoConsulta.LINKS) {
            verify(linkRepository, never()).findAllByProjeto_ProjetoId(any());
        } else if (tipoConsulta == TipoConsulta.TAGS) {
            verify(projetoConsultado, never()).getTags();
        }
    }

    @E("a visualização não deve alterar dados")
    public void verificarSomenteLeitura() {
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
        verify(projetoRepository, never()).save(any(ProjetoEntity.class));
        verify(tarefaRepository, never()).save(any(TarefaEntity.class));
        verify(tarefaRepository, never()).deleteById(any(UUID.class));
        verify(linkRepository, never()).save(any(LinkEntity.class));
        verify(tagRepository, never()).save(any(TagEntity.class));
    }

    private ProjetoEntity novoProjeto(Map<String, String> dados) {
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setProjetoId(uuid(dados.get("projetoId")));
        projeto.setUsuario(usuarioComId(USUARIO_AUTENTICADO_ID));
        projeto.setTitulo(dados.get("titulo"));
        projeto.setPrioridade(dados.get("prioridade"));
        projeto.setStatus(dados.get("status"));
        return projeto;
    }

    private TarefaEntity novaTarefa(Map<String, String> dados) {
        TarefaEntity tarefa = new TarefaEntity();
        tarefa.setTarefaId(uuid(dados.get("tarefaId")));
        tarefa.setProjeto(projetoComId(uuid(dados.get("projetoId"))));
        tarefa.setTitulo(dados.get("titulo"));
        tarefa.setPrioridade(dados.get("prioridade"));
        tarefa.setStatus(dados.get("status"));
        tarefa.setDataLimite(LocalDate.parse(dados.get("dataLimite")));
        return tarefa;
    }

    private LinkEntity novoLink(Map<String, String> dados) {
        LinkEntity link = new LinkEntity();
        link.setLinkId(uuid(dados.get("linkId")));
        link.setProjeto(projetoComId(uuid(dados.get("projetoId"))));
        link.setUrl(dados.get("url"));
        return link;
    }

    private UsuarioEntity usuarioComId(UUID usuarioId) {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUsuarioId(usuarioId);
        return usuario;
    }

    private ProjetoEntity projetoComId(UUID projetoId) {
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setProjetoId(projetoId);
        return projeto;
    }

    private TagEntity novaTag(Map<String, String> dados) {
        TagEntity tag = new TagEntity();
        tag.setTagId(uuid(dados.get("tagId")));
        tag.setNome(dados.get("nome"));
        return tag;
    }

    private void assertObjetoContem(Object objeto, Map<String, String> esperado) {
        Map<String, Object> valores = objectMapper.convertValue(objeto, new TypeReference<>() { });
        esperado.forEach((campo, valor) ->
                assertThat(String.valueOf(valores.get(campo))).isEqualTo(valor));
    }

    private UUID uuid(String valor) {
        return UUID.fromString(valor);
    }

    private enum TipoConsulta {
        PERFIL,
        PROJETOS,
        TAREFAS,
        LINKS,
        TAGS
    }
}

package com.ProjectSpace.bdd.steps;

import com.ProjectSpace.dto.request.TagRequestDTO;
import com.ProjectSpace.entity.ProjetoEntity;
import com.ProjectSpace.entity.TagEntity;
import com.ProjectSpace.repository.ProjetoRepository;
import com.ProjectSpace.repository.TagRepository;
import com.ProjectSpace.service.TagService;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TagSteps {

    private static final UUID USUARIO_AUTENTICADO_ID =
            UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private TagService tagService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final Map<UUID, ProjetoEntity> projetos = new HashMap<>();
    private final Map<UUID, Set<TagEntity>> tagsDosProjetos = new HashMap<>();
    private final Map<UUID, TagEntity> tags = new HashMap<>();
    private Map<String, String> dados;
    private Throwable erro;

    @Before
    public void prepararCenario() {
        MockitoAnnotations.openMocks(this);
        projetos.clear();
        tagsDosProjetos.clear();
        tags.clear();
        erro = null;
        when(tagRepository.save(any(TagEntity.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
    }

    @Dado("os dados para criar a tag:")
    @Dado("os dados para vincular a tag ao projeto:")
    public void informarDadosDaTag(DataTable tabela) {
        dados = valoresDa(tabela);
    }

    @E("existe uma tag com nome normalizado {string} e id {string}")
    public void existeTagComNome(String nome, String tagId) {
        TagEntity tag = novaTag(tagId);
        when(tagRepository.findByNomeIgnoreCase(nome)).thenReturn(Optional.of(tag));
    }

    @E("existe para a tag um projeto com id {string} e usuário {string}")
    public void existeProjeto(String projetoId, String usuarioId) {
        UUID id = uuid(projetoId);
        ProjetoEntity projeto = mock(ProjetoEntity.class);
        Set<TagEntity> tagsDoProjeto = new HashSet<>();
        when(projeto.getProjetoId()).thenReturn(id);
        when(projeto.getUsuarioId()).thenReturn(uuid(usuarioId));
        when(projeto.getTags()).thenReturn(tagsDoProjeto);
        when(projetoRepository.findById(id)).thenReturn(Optional.of(projeto));
        projetos.put(id, projeto);
        tagsDosProjetos.put(id, tagsDoProjeto);
    }

    @E("existe uma tag com id {string}")
    public void existeTag(String tagId) {
        TagEntity tag = novaTag(tagId);
        when(tagRepository.findById(uuid(tagId))).thenReturn(Optional.of(tag));
    }

    @E("a tag {string} já está vinculada ao projeto {string}")
    public void tagJaVinculada(String tagId, String projetoId) {
        tagsDosProjetos.get(uuid(projetoId)).add(tags.get(uuid(tagId)));
    }

    @Quando("crio a tag")
    public void criarTag() {
        TagRequestDTO request = objectMapper.convertValue(dados, TagRequestDTO.class);
        erro = catchThrowable(() -> tagService.criarTag(request));
    }

    @Quando("vinculo a tag ao projeto")
    public void vincularTag() {
        UUID projetoId = uuid(dados.get("projetoId"));
        UUID tagId = uuid(dados.get("tagId"));
        erro = catchThrowable(() ->
                tagService.vincularTagAoProjeto(USUARIO_AUTENTICADO_ID, projetoId, tagId));
    }

    @Entao("deve salvar a tag com os valores:")
    public void verificarTagSalva(DataTable tabela) {
        Map<String, String> esperado = valoresDa(tabela);
        ArgumentCaptor<TagEntity> captor = ArgumentCaptor.forClass(TagEntity.class);

        assertThat(erro).isNull();
        verify(tagRepository).save(captor.capture());
        esperado.forEach((campo, valor) ->
                assertThat(captor.getValue()).extracting(campo).isEqualTo(valor));
        assertThat(captor.getValue().getTagId()).isNull();
    }

    @E("o método de criação da tag não deve retornar conteúdo")
    public void verificarRetornoVoid() throws NoSuchMethodException {
        assertThat(TagService.class
                .getDeclaredMethod("criarTag", TagRequestDTO.class)
                .getReturnType()).isEqualTo(void.class);
    }

    @Entao("a operação de tag deve ser rejeitada com {string} sem salvar")
    public void verificarOperacaoRejeitada(String mensagem) {
        assertThat(erro).isNotNull().hasMessage(mensagem);
        verify(tagRepository, never()).save(any(TagEntity.class));
        verify(projetoRepository, never()).save(any(ProjetoEntity.class));
    }

    @Entao("deve vincular somente a tag {string} ao projeto {string}")
    public void verificarTagVinculada(String tagId, String projetoId) {
        UUID projetoUuid = uuid(projetoId);
        assertThat(erro).isNull();
        verify(projetoRepository).save(projetos.get(projetoUuid));
        assertThat(tagsDosProjetos.get(projetoUuid)).containsExactly(tags.get(uuid(tagId)));
        verify(tagRepository, never()).save(any(TagEntity.class));
    }

    private TagEntity novaTag(String tagId) {
        UUID id = uuid(tagId);
        TagEntity tag = mock(TagEntity.class);
        when(tag.getTagId()).thenReturn(id);
        tags.put(id, tag);
        return tag;
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

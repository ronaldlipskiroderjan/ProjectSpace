
package com.ProjectSpace.service;

import com.ProjectSpace.database.model.ProjetoEntity;
import com.ProjectSpace.database.model.TagEntity;
import com.ProjectSpace.database.repository.ProjetoRepository;
import com.ProjectSpace.database.repository.TagRepository;
import com.ProjectSpace.dto.request.TagRequestDTO;
import com.ProjectSpace.dto.response.TagResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ProjetoRepository projetoRepository;
    private final UsuarioService usuarioService;

    public List<TagResponseDTO> listarTags(String username, UUID projetoId) {

        ProjetoEntity projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("PROJETO_NAO_ENCONTRADO"));

        var usuario = usuarioService.buscarUsuarioAutenticado(username);

        if (!Objects.equals(
                projeto.getUsuario().getUsuarioId(),
                usuario.getUsuarioId())) {
            throw new IllegalArgumentException("ACESSO_NEGADO");
        }

        return projeto.getTags()
                .stream()
                .map(tag -> new TagResponseDTO(
                        tag.getTagId(),
                        tag.getNome()
                ))
                .toList();
    }

    public void criarTag(TagRequestDTO request) {

        if (request.nome() == null || request.nome().isBlank()) {
            throw new IllegalArgumentException("NOME_OBRIGATORIO");
        }

        String nomeNormalizado = request.nome()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (tagRepository.findByNomeIgnoreCase(nomeNormalizado).isPresent()) {
            throw new IllegalArgumentException("TAG_DUPLICADA");
        }

        TagEntity tag = TagEntity.builder()
                .nome(nomeNormalizado)
                .build();

        tagRepository.save(tag);
    }

    public void vincularTagAoProjeto(String username, UUID projetoId, UUID tagId) {

        ProjetoEntity projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("PROJETO_NAO_ENCONTRADO"));

        var usuario = usuarioService.buscarUsuarioAutenticado(username);

        if (!Objects.equals(
                projeto.getUsuario().getUsuarioId(),
                usuario.getUsuarioId())) {
            throw new IllegalArgumentException("ACESSO_NEGADO");
        }

        TagEntity tag = tagRepository.findById(tagId)
                .orElseThrow(() ->
                        new IllegalArgumentException("TAG_NAO_ENCONTRADA"));

        if (projeto.getTags().contains(tag)) {
            throw new IllegalArgumentException("TAG_JA_VINCULADA");
        }

        projeto.getTags().add(tag);

        projetoRepository.save(projeto);
    }
}

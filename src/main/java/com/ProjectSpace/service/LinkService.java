package com.ProjectSpace.service;

import com.ProjectSpace.database.repository.LinkRepository;
import com.ProjectSpace.database.repository.ProjetoRepository;
import com.ProjectSpace.dto.request.LinkRequestDTO;
import com.ProjectSpace.dto.response.LinkResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ProjectSpace.database.model.LinkEntity;
import com.ProjectSpace.database.model.ProjetoEntity;


import java.util.stream.Collectors;

import java.util.Objects;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final ProjetoRepository projetoRepository;
    private final UsuarioService usuarioService;

    public List<LinkResponseDTO> listarLinks(String username, UUID projetoId) {

    ProjetoEntity projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() ->
                    new IllegalArgumentException("PROJETO_NAO_ENCONTRADO"));

    var usuario = usuarioService.buscarUsuarioAutenticado(username);

    if (!Objects.equals(
            projeto.getUsuario().getUsuarioId(),
            usuario.getUsuarioId())) {
        throw new IllegalArgumentException("ACESSO_NEGADO");
    }

    return linkRepository.findAllByProjeto_ProjetoId(projetoId)
            .stream()
            .map(link -> new LinkResponseDTO(
                    link.getLinkId(),
                    link.getProjeto().getProjetoId(),
                    link.getUrl()
            ))
            .collect(Collectors.toList());
}

    public void adicionarLink(String username, LinkRequestDTO request) {

    ProjetoEntity projeto = projetoRepository.findById(request.projetoId())
            .orElseThrow(() ->
                    new IllegalArgumentException("PROJETO_NAO_ENCONTRADO"));

    var usuario = usuarioService.buscarUsuarioAutenticado(username);

    if (!Objects.equals(
            projeto.getUsuario().getUsuarioId(),
            usuario.getUsuarioId())) {
        throw new IllegalArgumentException("ACESSO_NEGADO");
    }

    LinkEntity link = LinkEntity.builder()
            .projeto(projeto)
            .url(request.url())
            .build();

    linkRepository.save(link);
}
}

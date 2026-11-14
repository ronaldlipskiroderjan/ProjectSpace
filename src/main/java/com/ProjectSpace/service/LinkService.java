package com.ProjectSpace.service;

import com.ProjectSpace.database.repository.LinkRepository;
import com.ProjectSpace.database.repository.ProjetoRepository;
import com.ProjectSpace.dto.request.LinkRequestDTO;
import com.ProjectSpace.dto.response.LinkResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final ProjetoRepository projetoRepository;

    public List<LinkResponseDTO> listarLinks(UUID usuarioAutenticadoId, UUID projetoId) {
        return null;
    }

    public void adicionarLink(UUID usuarioAutenticadoId, LinkRequestDTO request) {
    }
}

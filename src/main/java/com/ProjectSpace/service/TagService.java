package com.ProjectSpace.service;

import com.ProjectSpace.database.repository.ProjetoRepository;
import com.ProjectSpace.database.repository.TagRepository;
import com.ProjectSpace.dto.request.TagRequestDTO;
import com.ProjectSpace.dto.response.TagResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ProjetoRepository projetoRepository;
    private final UsuarioService usuarioService;

    public List<TagResponseDTO> listarTags(String username, UUID projetoId) {
        return null;
    }

    public void criarTag(TagRequestDTO request) {
    }

    public void vincularTagAoProjeto(String username, UUID projetoId, UUID tagId) {
    }
}

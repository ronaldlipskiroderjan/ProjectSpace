package com.ProjectSpace.service;

import com.ProjectSpace.database.repository.ProjetoRepository;
import com.ProjectSpace.dto.request.ProjetoRequestDTO;
import com.ProjectSpace.dto.response.ProjetoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public List<ProjetoResponseDTO> listarProjetos(UUID usuarioAutenticadoId) {
        return null;
    }

    public void criarProjeto(UUID usuarioAutenticadoId, ProjetoRequestDTO request) {
    }
}

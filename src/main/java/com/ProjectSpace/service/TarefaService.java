package com.ProjectSpace.service;

import com.ProjectSpace.database.repository.ProjetoRepository;
import com.ProjectSpace.database.repository.TarefaRepository;
import com.ProjectSpace.dto.request.TarefaRequestDTO;
import com.ProjectSpace.dto.response.TarefaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final ProjetoRepository projetoRepository;

    public List<TarefaResponseDTO> listarTarefas(UUID usuarioAutenticadoId, UUID projetoId) {
        return null;
    }

    public void adicionarTarefa(UUID usuarioAutenticadoId, TarefaRequestDTO request) {
    }

    public TarefaResponseDTO editarTarefa(
            UUID usuarioAutenticadoId,
            UUID tarefaId,
            TarefaRequestDTO request) {
        return null;
    }

    public void excluirTarefa(UUID usuarioAutenticadoId, UUID tarefaId) {
    }
}

package com.ProjectSpace.service;

import com.ProjectSpace.database.model.ProjetoEntity;
import com.ProjectSpace.database.model.TarefaEntity;
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
        if (request.titulo() == null || request.titulo().isBlank()) {
            throw new IllegalArgumentException("TITULO_OBRIGATORIO");
        }
        ProjetoEntity projeto = projetoRepository.findById(request.projetoId())
                .orElseThrow(() -> new IllegalArgumentException("PROJETO_NAO_ENCONTRADO"));
        if (!projeto.getUsuario().getUsuarioId().equals(usuarioAutenticadoId)) {
            throw new IllegalArgumentException("ACESSO_NEGADO");
        }
        TarefaEntity tarefa = new TarefaEntity();
        tarefa.setTarefaId(null);
        tarefa.setProjeto(projeto);
        tarefa.setTitulo(request.titulo());
        tarefa.setPrioridade(request.prioridade());
        tarefa.setStatus(request.status());
        tarefa.setDataLimite(request.dataLimite());
        tarefaRepository.save(tarefa);
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

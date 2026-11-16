package com.ProjectSpace.service;

import com.ProjectSpace.database.model.ProjetoEntity;
import com.ProjectSpace.database.model.TarefaEntity;
import com.ProjectSpace.database.model.UsuarioEntity;
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
    private final UsuarioService usuarioService;

    public List<TarefaResponseDTO> listarTarefas(String username, UUID projetoId) {
        return null;
    }

    public void adicionarTarefa(String username, TarefaRequestDTO request) {
        if (request.titulo() == null || request.titulo().isBlank()) {
            throw new IllegalArgumentException("TITULO_OBRIGATORIO");
        }
        UsuarioEntity usuario = usuarioService.buscarUsuarioAutenticado(username);
        ProjetoEntity projeto = projetoRepository.findById(request.projetoId())
                .orElseThrow(() -> new IllegalArgumentException("PROJETO_NAO_ENCONTRADO"));
        if (!projeto.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())) {
            throw new IllegalArgumentException("ACESSO_NEGADO");
        }
        TarefaEntity tarefa = new TarefaEntity();
        tarefa.setProjeto(projeto);
        tarefa.setTitulo(request.titulo());
        tarefa.setPrioridade(request.prioridade());
        tarefa.setStatus(request.status());
        tarefa.setDataLimite(request.dataLimite());
        tarefaRepository.save(tarefa);
    }

    public TarefaResponseDTO editarTarefa(
            String username,
            UUID tarefaId,
            TarefaRequestDTO request) {
        return null;
    }

    public void excluirTarefa(String username, UUID tarefaId) {
    }
}

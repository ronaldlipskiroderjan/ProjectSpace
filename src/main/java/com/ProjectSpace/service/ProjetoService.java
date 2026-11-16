package com.ProjectSpace.service;

import com.ProjectSpace.database.model.ProjetoEntity;
import com.ProjectSpace.database.model.UsuarioEntity;
import com.ProjectSpace.database.repository.ProjetoRepository;
import com.ProjectSpace.dto.request.ProjetoRequestDTO;
import com.ProjectSpace.dto.response.ProjetoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private static final Set<String> PRIORIDADES_VALIDAS = Set.of("baixa", "média", "alta");

    private final ProjetoRepository projetoRepository;

    public List<ProjetoResponseDTO> listarProjetos(UUID usuarioAutenticadoId) {
        if (usuarioAutenticadoId == null) {
            throw new IllegalArgumentException("NAO_AUTENTICADO");
        }
        List<ProjetoEntity> projetos = projetoRepository.findAllByUsuario_UsuarioId(usuarioAutenticadoId);
        return projetos.stream()
                .map(p -> new ProjetoResponseDTO(p.getProjetoId(), p.getTitulo(), p.getPrioridade(), p.getStatus()))
                .toList();
    }

    public void criarProjeto(UUID usuarioAutenticadoId, ProjetoRequestDTO request) {
        if (usuarioAutenticadoId == null) {
            throw new IllegalArgumentException("NAO_AUTENTICADO");
        }
        if (request.titulo() == null || request.titulo().isBlank()) {
            throw new IllegalArgumentException("TITULO_OBRIGATORIO");
        }
        if (request.prioridade() == null || !PRIORIDADES_VALIDAS.contains(request.prioridade())) {
            throw new IllegalArgumentException("PRIORIDADE_INVALIDA");
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUsuarioId(usuarioAutenticadoId);

        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setProjetoId(null);
        projeto.setTitulo(request.titulo());
        projeto.setPrioridade(request.prioridade());
        projeto.setStatus(request.status());
        projeto.setUsuario(usuario);

        projetoRepository.save(projeto);
    }
}

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

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private static final Set<String> PRIORIDADES_VALIDAS = Set.of("baixa", "média", "alta");

    private final ProjetoRepository projetoRepository;
    private final UsuarioService usuarioService;

    public List<ProjetoResponseDTO> listarProjetos(String username) {
        UsuarioEntity usuario = usuarioService.buscarUsuarioAutenticado(username);
        List<ProjetoEntity> projetos = projetoRepository.findAllByUsuario_UsuarioId(usuario.getUsuarioId());
        return projetos.stream()
                .map(this::toDTO)
                .toList();
    }

    public void criarProjeto(String username, ProjetoRequestDTO request) {
        UsuarioEntity usuario = usuarioService.buscarUsuarioAutenticado(username);
        if (request.titulo() == null || request.titulo().isBlank()) {
            throw new IllegalArgumentException("TITULO_OBRIGATORIO");
        }
        if (request.prioridade() == null || !PRIORIDADES_VALIDAS.contains(request.prioridade())) {
            throw new IllegalArgumentException("PRIORIDADE_INVALIDA");
        }
        ProjetoEntity projeto = new ProjetoEntity();
        projeto.setTitulo(request.titulo());
        projeto.setPrioridade(request.prioridade());
        projeto.setStatus(request.status());
        projeto.setUsuario(usuario);

        projetoRepository.save(projeto);
    }

    private ProjetoResponseDTO toDTO(ProjetoEntity projeto) {
        return new ProjetoResponseDTO(
                projeto.getProjetoId(),
                projeto.getTitulo(),
                projeto.getPrioridade(),
                projeto.getStatus());
    }
}

package com.ProjectSpace.service;

import com.ProjectSpace.database.repository.UsuarioRepository;
import com.ProjectSpace.dto.request.UsuarioRequestDTO;
import com.ProjectSpace.dto.response.UsuarioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO buscarPerfil(UUID usuarioAutenticadoId) {
        return null;
    }

    public UsuarioResponseDTO editarUsuario(
            UUID usuarioAutenticadoId,
            UUID usuarioId,
            UsuarioRequestDTO request) {
        return null;
    }
}

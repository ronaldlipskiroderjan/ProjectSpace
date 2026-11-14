package com.ProjectSpace.service;

import com.ProjectSpace.config.TokenProvider;
import com.ProjectSpace.database.repository.UsuarioRepository;
import com.ProjectSpace.dto.request.UsuarioRequestDTO;
import com.ProjectSpace.dto.response.TokenResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public TokenResponseDTO criarUsuario(UsuarioRequestDTO request) {
        return null;
    }
}

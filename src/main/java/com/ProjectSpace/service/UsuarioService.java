package com.ProjectSpace.service;

import com.ProjectSpace.database.model.UsuarioEntity;
import com.ProjectSpace.database.repository.UsuarioRepository;
import com.ProjectSpace.dto.request.UsuarioRequestDTO;
import com.ProjectSpace.dto.response.UsuarioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO buscarPerfil(String emailUsuarioAutenticado) {
        return toDTO(buscarUsuarioAutenticado(emailUsuarioAutenticado));
    }

    public UsuarioResponseDTO editarUsuario(
            String emailUsuarioAutenticado,
            UsuarioRequestDTO request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new IllegalArgumentException("NOME_OBRIGATORIO");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("EMAIL_OBRIGATORIO");
        }

        String emailNormalizado = request.email().trim().toLowerCase(Locale.ROOT);
        if (!EMAIL_PATTERN.matcher(emailNormalizado).matches()) {
            throw new IllegalArgumentException("EMAIL_INVALIDO");
        }

        UsuarioEntity usuario = buscarUsuarioAutenticado(emailUsuarioAutenticado);

        if (!emailNormalizado.equalsIgnoreCase(usuario.getEmail())) {
            usuarioRepository.findByEmailIgnoreCase(emailNormalizado)
                    .filter(usuarioComEmail -> !Objects.equals(
                            usuarioComEmail.getUsuarioId(), usuario.getUsuarioId()))
                    .ifPresent(usuarioComEmail -> {
                        throw new IllegalArgumentException("EMAIL_DUPLICADO");
                    });
        }

        usuario.setNome(request.nome());
        usuario.setEmail(emailNormalizado);
        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuario);

        return toDTO(usuarioSalvo);
    }

    private UsuarioEntity buscarUsuarioAutenticado(String emailUsuarioAutenticado) {
        if (emailUsuarioAutenticado == null || emailUsuarioAutenticado.isBlank()) {
            throw new IllegalArgumentException("USUARIO_NAO_ENCONTRADO");
        }

        String emailNormalizado = emailUsuarioAutenticado.trim().toLowerCase(Locale.ROOT);
        return usuarioRepository.findByEmailIgnoreCase(emailNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("USUARIO_NAO_ENCONTRADO"));
    }

    private UsuarioResponseDTO toDTO(UsuarioEntity usuario) {
        return new UsuarioResponseDTO(
                usuario.getUsuarioId(),
                usuario.getNome(),
                usuario.getEmail());
    }
}

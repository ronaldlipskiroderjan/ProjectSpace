package com.ProjectSpace.service;

import com.ProjectSpace.config.TokenProvider;
import com.ProjectSpace.database.model.RoleEntity;
import com.ProjectSpace.database.model.UsuarioEntity;
import com.ProjectSpace.database.repository.RoleRepository;
import com.ProjectSpace.database.repository.UsuarioRepository;
import com.ProjectSpace.dto.request.UsuarioRequestDTO;
import com.ProjectSpace.dto.response.TokenResponseDTO;
import com.ProjectSpace.enums.RoleTypeEnum;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt.expiration}")
    private long expirationTime;

    public TokenResponseDTO criarUsuario(UsuarioRequestDTO request) throws Exception {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BadRequestException("NOME_OBRIGATORIO");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BadRequestException("EMAIL_OBRIGATORIO");
        }
        if (request.senha() == null || request.senha().length() < 8) {
            throw new BadRequestException("SENHA_CURTA");
        }

        String emailNormalizado = request.email().trim().toLowerCase(Locale.ROOT);
        if (usuarioRepository.existsByEmailIgnoreCase(emailNormalizado)) {
            throw new BadRequestException("EMAIL_DUPLICADO");
        }

        RoleEntity newRole = roleRepository.findByNome(RoleTypeEnum.ROLE_USER.name())
                .orElseGet(() -> roleRepository.save(RoleEntity.builder()
                        .nome(RoleTypeEnum.ROLE_USER.name())
                        .build()));
        usuarioRepository.save(UsuarioEntity.builder()
                        .nome(request.nome())
                        .email(emailNormalizado)
                        .senhaHash(passwordEncoder.encode(request.senha()))
                        .roles(Set.of(newRole))
                .build());
        return returnToken(emailNormalizado, request.senha());
    }


    private TokenResponseDTO returnToken(String email, String senha) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email.toLowerCase(), senha));
        String token = tokenProvider.gerarToken(auth);
        return new TokenResponseDTO(token, expirationTime);
    }
}

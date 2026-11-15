package com.ProjectSpace.controller;

import com.ProjectSpace.dto.request.UsuarioRequestDTO;
import com.ProjectSpace.dto.response.UsuarioResponseDTO;
import com.ProjectSpace.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("me")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResponseDTO buscarUsuario(Authentication authentication) {
        return usuarioService.buscarPerfil(authentication.getName());
    }

    @PutMapping("me")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResponseDTO editarUsuario(
            Authentication authentication,
            @Valid @RequestBody UsuarioRequestDTO request) {
        return usuarioService.editarUsuario(authentication.getName(), request);
    }
}

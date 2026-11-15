package com.ProjectSpace.controller;

import com.ProjectSpace.dto.request.UsuarioRequestDTO;
import com.ProjectSpace.dto.response.TokenResponseDTO;
import com.ProjectSpace.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseDTO criarUsuario(@Valid @RequestBody UsuarioRequestDTO request) throws Exception {
        return authService.criarUsuario(request);
    }
}

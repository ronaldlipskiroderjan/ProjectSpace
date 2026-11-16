package com.ProjectSpace.controller;

import com.ProjectSpace.dto.request.ProjetoRequestDTO;
import com.ProjectSpace.dto.response.ProjetoResponseDTO;
import com.ProjectSpace.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProjetoResponseDTO> listarProjetos(Authentication authentication) {
        return projetoService.listarProjetos(authentication.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarProjeto(
            Authentication authentication,
            @Valid @RequestBody ProjetoRequestDTO request) {
        projetoService.criarProjeto(authentication.getName(), request);
    }
}

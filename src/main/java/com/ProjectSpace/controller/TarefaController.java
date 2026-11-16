package com.ProjectSpace.controller;

import com.ProjectSpace.dto.request.TarefaRequestDTO;
import com.ProjectSpace.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionarTarefa(
            Authentication authentication,
            @Valid @RequestBody TarefaRequestDTO request) {
        tarefaService.adicionarTarefa(authentication.getName(), request);
    }
}

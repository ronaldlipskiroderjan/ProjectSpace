package com.ProjectSpace.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record TarefaRequestDTO(
        UUID projetoId,
        String titulo,
        String prioridade,
        String status,
        LocalDate dataLimite) {
}

package com.ProjectSpace.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record TarefaResponseDTO(
        UUID tarefaId,
        UUID projetoId,
        String titulo,
        String prioridade,
        String status,
        LocalDate dataLimite) {
}

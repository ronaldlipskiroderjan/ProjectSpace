package com.ProjectSpace.dto.response;

import java.util.UUID;

public record ProjetoResponseDTO(
        UUID projetoId,
        String titulo,
        String prioridade,
        String status) {
}

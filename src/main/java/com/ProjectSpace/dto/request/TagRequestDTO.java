package com.ProjectSpace.dto.request;

import java.util.UUID;

public record TagRequestDTO(
        String nome,
        UUID projetoId,
        UUID tagId
) {
}

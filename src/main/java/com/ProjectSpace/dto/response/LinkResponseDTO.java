package com.ProjectSpace.dto.response;

import java.util.UUID;

public record LinkResponseDTO(UUID linkId, UUID projetoId, String url) {
}

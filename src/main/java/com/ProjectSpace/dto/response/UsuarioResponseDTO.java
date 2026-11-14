package com.ProjectSpace.dto.response;

import java.util.UUID;

public record UsuarioResponseDTO(UUID usuarioId, String nome, String email) {
}

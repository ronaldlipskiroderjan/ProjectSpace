package com.ProjectSpace.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "NOME_OBRIGATORIO")
        @Size(max = 255, message = "NOME_MUITO_LONGO")
        String nome,
        @NotBlank(message = "EMAIL_OBRIGATORIO")
        @Email(message = "EMAIL_INVALIDO")
        @Size(max = 255, message = "EMAIL_MUITO_LONGO")
        String email,
        @Size(min = 8, message = "SENHA_CURTA")
        String senha
) {
}

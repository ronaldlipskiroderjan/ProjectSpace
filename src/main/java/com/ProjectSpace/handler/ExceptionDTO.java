package com.ProjectSpace.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public record ExceptionDTO(
        String mensagem,
        int code,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Map<String, String> campos
) {

    public ExceptionDTO(String mensagem, int code) {
        this(mensagem, code, null);
    }
}

package com.ProjectSpace.handler;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> dtoValidationException(
            MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();

        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(
                "VALIDACAO_DTO",
                HttpStatus.BAD_REQUEST.value(),
                campos
        ));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDTO> badRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDTO> illegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> exception(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDTO(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
    }
}

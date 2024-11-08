package org.ong.pet.pex.backendpetx.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.ong.pet.pex.backendpetx.service.exceptions.*;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    private HttpStatus status;
    private StandardError err = new StandardError();

    @ExceptionHandler(UsuarioNaoEncontrado.class)
    public ResponseEntity<StandardError> usuarioNaoEncontrado(UsuarioNaoEncontrado e, HttpServletRequest request) {
        status = HttpStatus.NOT_FOUND;

        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Resource not found");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> argumentoInvalido(MethodArgumentNotValidException e, HttpServletRequest request) {

        HttpStatus statusValidationError = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError error = new ValidationError();
        error.setTimestamp(Instant.now());
        error.setStatus(statusValidationError.value());
        error.setError("Validation exception");
        error.setMessage("uma ou mais validações falharam");
        error.setPath(request.getRequestURI());
        // vai pegar os errors especificos na validação
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            error.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(statusValidationError).body(error);
    }

    @ExceptionHandler(UsuarioJaCadastrado.class)
    public ResponseEntity<StandardError> usuarioJaCadastrado(UsuarioJaCadastrado e, HttpServletRequest request) {
        status = HttpStatus.BAD_REQUEST;

        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Resource not found");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}

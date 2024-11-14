package org.ong.pet.pex.backendpetx.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.ong.pet.pex.backendpetx.entities.Ong;
import org.ong.pet.pex.backendpetx.service.exceptions.AnimalJaCadastrado;
import org.ong.pet.pex.backendpetx.service.exceptions.AnimalNaoEncontrado;
import org.ong.pet.pex.backendpetx.service.exceptions.OngNaoEncontrada;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioJaCadastrado;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioNaoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        err.setError("Recurso nao encontrado");
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
        error.setError("Erro de validação");
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
        err.setError("Recurso nao encontrado");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AnimalJaCadastrado.class)
    public ResponseEntity<StandardError> AnimalJaCadastrado(AnimalJaCadastrado e, HttpServletRequest request) {
        status = HttpStatus.BAD_REQUEST;

        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Recurso nao encontrado");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AnimalNaoEncontrado.class)
    public ResponseEntity<StandardError> AnimalNaoEncontrado(AnimalNaoEncontrado e, HttpServletRequest request) {
        status = HttpStatus.BAD_REQUEST;

        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Recurso nao encontrado");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(OngNaoEncontrada.class)
    public ResponseEntity<StandardError> usuarioJaCadastrado(OngNaoEncontrada e, HttpServletRequest request) {
        status = HttpStatus.BAD_REQUEST;

        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Ong Não encontrada");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}

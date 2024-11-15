package org.ong.pet.pex.backendpetx.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.exceptions.TutorException;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {


    @ExceptionHandler(PetXException.class)
    public ResponseEntity<StandardError> manipularPetXException(PetXException pe, HttpServletRequest request) {
        HttpStatus status = pe.getStatus();
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(pe.getStatus().value());
        error.setError("Invalid Request");
        error.setMessage(pe.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<StandardError> manipularUsuarioException(UsuarioException ex, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(ex.getStatus().value());
        error.setError("Acesso Negado");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> manipularValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ValidationError error = new ValidationError();
        error.setTimestamp(Instant.now());
        error.setStatus(ex.getStatusCode().value());
        error.setError("Validation Error");
        error.setMessage("Erro de validação nos campos");
        error.setPath(request.getRequestURI());

        // Adiciona os erros de campo na resposta
        for (FieldError f : ex.getBindingResult().getFieldErrors()) {
            error.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(ex.getStatusCode().value()).body(error);
    }

    @ExceptionHandler(TutorException.class)
    public ResponseEntity<StandardError> manipularTutorException(TutorException tx, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(tx.getStatus().value());
        error.setError("Acesso Negado");
        error.setMessage(tx.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(tx.getStatus().value()).body(error);
    }
}

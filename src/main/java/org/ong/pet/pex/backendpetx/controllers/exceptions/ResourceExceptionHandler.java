package org.ong.pet.pex.backendpetx.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.ong.pet.pex.backendpetx.service.exceptions.*;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
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
        error.setError("Requisição Inválida");
        error.setMessage(pe.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<StandardError> manipularUsuarioException(UsuarioException ex, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(ex.getStatus().value());
        error.setError("Recurso Não Encontrado");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> manipularValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ValidationError error = new ValidationError();
        error.setTimestamp(Instant.now());
        error.setStatus(ex.getStatusCode().value());
        error.setError("Erro de Validação");
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
        error.setError("Recurso Não Encontrado");
        error.setMessage(tx.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(tx.getStatus().value()).body(error);
    }

    @ExceptionHandler(EstoqueException.class)
    public ResponseEntity<StandardError> manipularEstoqueException(EstoqueException ex, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(ex.getStatus().value());
        error.setError("Recurso Não Encontrado");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(ex.getStatus().value()).body(error);
    }

    @ExceptionHandler(EnumException.class)
    public ResponseEntity<ValidationError> manipularEnumException(EnumException ex, HttpServletRequest request) {
        ValidationError error = new ValidationError();
        HttpStatus status = ex.getStatus();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Erro de Validação");
        error.setMessage("Erro de validação nos campos");
        error.setPath(request.getRequestURI());

        error.addError(ex.getCampoErro(), ex.getMessage());

        return ResponseEntity.status(status.value()).body(error);
    }

    @ExceptionHandler(DespesaException.class)
    public ResponseEntity<StandardError> manipularEstoqueException(DespesaException ex, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(ex.getStatus().value());
        error.setError("Recurso Não Encontrado");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(ex.getStatus().value()).body(error);
    }


    @ExceptionHandler(AuthException.class)
    public ResponseEntity<StandardError> manipularAuthException(AuthException ex, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Recurso Não Encontrado");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ProdutoException.class)
    public ResponseEntity<StandardError> manipularProdutoException(ProdutoException ex, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Recurso Não Encontrado");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConsumoAlimentoException.class)
    public ResponseEntity<StandardError> manipularProdutoException(ConsumoAlimentoException ex, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Recurso Não Encontrado");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // essa exceção é lançada quando ocorre um erro de conversão de um parâmetro o campo pode estar certo mas se o valor estiver errado é ela que pega e trata o erro
    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<StandardError> manipularErroConversao(ConversionFailedException ex, HttpServletRequest request) {

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Erro de Conversão");
        error.setPath(request.getRequestURI());

        // Tenta capturar o tipo do campo que falhou na conversão
        String errorMessage = "Falha de conversão no parâmetro.";

        // Verifica se há uma causa mais específica e tenta obter o nome do campo
        Throwable cause = ex.getCause();
        if (cause instanceof IllegalArgumentException) {
            // O erro de conversão geralmente contém detalhes na causa original
            errorMessage = cause.getMessage();
        }

        // Obter o TypeDescriptor do parâmetro que falhou
        TypeDescriptor targetType = ex.getTargetType();

        // Verifica se o tipo de destino é um Enum
        if (targetType.getObjectType().isEnum()) {
            // Obter os valores aceitos para o Enum
            String acceptedValues = getAcceptedEnumValues(targetType.getObjectType());
            errorMessage = "Erro ao converter Enum os valores aceitos para " + targetType.getObjectType().getSimpleName() + " são: " + acceptedValues;
        } else {
            errorMessage = " Tipo de parâmetro não enum.";
        }

        error.setMessage(errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Metodo para obter os valores aceitos de um Enum
    private String getAcceptedEnumValues(Class<?> enumClass) {
        if (enumClass.isEnum()) {
            Object[] enumValues = enumClass.getEnumConstants();
            StringBuilder acceptedValues = new StringBuilder();
            for (Object enumValue : enumValues) {
                acceptedValues.append(enumValue.toString()).append(", ");
            }
            if (!acceptedValues.isEmpty()) {
                acceptedValues.setLength(acceptedValues.length() - 2); // Remove a última vírgula
            }
            return acceptedValues.toString();
        }
        return "Nenhum valor aceito encontrado para o tipo fornecido.";
    }
}
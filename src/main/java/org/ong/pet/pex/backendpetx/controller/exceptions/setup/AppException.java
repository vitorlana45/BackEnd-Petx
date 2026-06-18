package org.ong.pet.pex.backendpetx.controller.exceptions.setup;

import org.springframework.http.HttpStatus;

// Exceção concreta “padrão” de domínio
public class AppException extends BaseApplicationError {
    public AppException(HttpStatus status, String messageKey, Object... args) {
        super(status, messageKey, args);
    }

    // fábricas de uso comum
    public static AppException animalNaoEncontrado(Object id) {
        return new AppException(HttpStatus.NOT_FOUND, "animal.nao.encontrado", id);
    }
    public static AppException chipDuplicado(String chip) {
        return new AppException(HttpStatus.CONFLICT, "animal.chip.duplicado", chip);
    }
}

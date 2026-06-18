package org.ong.pet.pex.backendpetx.controllers.exceptions.setup;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseApplicationError extends RuntimeException {
    private final HttpStatus status;
    private final String messageKey;
    private final Object[] messageArgs;

    protected BaseApplicationError(HttpStatus status, String messageKey, Object[] args) {
        super(messageKey); // guardamos a key no getMessage()
        this.status = status;
        this.messageKey = messageKey;
        this.messageArgs = args == null ? new Object[0] : args;
    }
}

package org.ong.pet.pex.backendpetx.service.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidacaoFiltroParametroException extends RuntimeException {

    private HttpStatus status;

    public ValidacaoFiltroParametroException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

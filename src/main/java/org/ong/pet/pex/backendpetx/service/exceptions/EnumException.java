package org.ong.pet.pex.backendpetx.service.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EnumException extends RuntimeException {

    private final HttpStatus status; // Adiciona o status
    private final String campoErro;

    public EnumException(String mensagem, HttpStatus status, String campoErro) {
        super(mensagem);
        this.campoErro = campoErro;
        this.status = status;
    }
}
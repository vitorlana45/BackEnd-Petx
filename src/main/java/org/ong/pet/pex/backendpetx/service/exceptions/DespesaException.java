package org.ong.pet.pex.backendpetx.service.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DespesaException extends RuntimeException {

    private final HttpStatus status;

    public DespesaException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static DespesaException despesaNaoEncontrada() {
        return new DespesaException("Despesa nao encontrado", HttpStatus.NOT_FOUND);
    }

    public static DespesaException filtroDespesaInvalido(final String filtro) {
        return new DespesaException(String.format("filtro '%s' nao encontrado ", filtro), HttpStatus.BAD_REQUEST);
    }
}
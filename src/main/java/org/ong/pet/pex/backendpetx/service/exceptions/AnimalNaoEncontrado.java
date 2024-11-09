package org.ong.pet.pex.backendpetx.service.exceptions;

import java.io.Serial;

public class AnimalNaoEncontrado extends RuntimeException {

    @Serial
    private final static long serialVersionUID = 1L;

    public AnimalNaoEncontrado(String message) {
        super(message);
    }
}

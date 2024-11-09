package org.ong.pet.pex.backendpetx.service.exceptions;

import java.io.Serial;

public class AnimalJaCadastrado extends RuntimeException {

    @Serial
    private final static long serialVersionUID = 1L;

    public AnimalJaCadastrado(String message) {
        super(message);
    }
}

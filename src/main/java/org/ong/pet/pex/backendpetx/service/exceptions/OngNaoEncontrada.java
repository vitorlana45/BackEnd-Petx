package org.ong.pet.pex.backendpetx.service.exceptions;

import java.io.Serial;

public class OngNaoEncontrada extends RuntimeException {

    @Serial
    private final static long serialVersionUID = 1L;

    public OngNaoEncontrada(String msg) {
        super(msg);
    }
}

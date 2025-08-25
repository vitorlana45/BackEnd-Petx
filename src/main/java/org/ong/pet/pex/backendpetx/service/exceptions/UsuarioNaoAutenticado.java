package org.ong.pet.pex.backendpetx.service.exceptions;


public class UsuarioNaoAutenticado extends RuntimeException {
    public UsuarioNaoAutenticado(String message) {
        super(message);
    }
}

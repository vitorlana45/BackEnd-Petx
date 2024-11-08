package org.ong.pet.pex.backendpetx.service.exceptions;

public class UsuarioJaCadastrado extends RuntimeException {
    public UsuarioJaCadastrado(String email) {
        super("Usuário já cadastrado para o email " + email);
    }
}

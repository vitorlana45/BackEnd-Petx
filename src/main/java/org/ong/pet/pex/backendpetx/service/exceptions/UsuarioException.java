package org.ong.pet.pex.backendpetx.service.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UsuarioException extends RuntimeException {

    private final HttpStatus status;

    public UsuarioException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }

    public UsuarioException(String mensagem, Map<String, String> erros, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }

    public static UsuarioException recursoNaoEncontrado(String nomeRecurso, Object identificador) {
        throw new UsuarioException(String.format("%s com ID '%s' não encontrado", nomeRecurso, identificador), HttpStatus.NOT_FOUND);
    }

    public static UsuarioException jaExiste(String nomeRecurso,String campoErro, Object identificador) {
        throw new UsuarioException(String.format("%s com %s '%s' já existe", nomeRecurso,campoErro, identificador), HttpStatus.CONFLICT);
    }

    public static UsuarioException dadosInvalidos(Map<String, String> erros) {
        throw new UsuarioException("Dados inválidos", erros, HttpStatus.BAD_REQUEST);
    }

    public static UsuarioException usuarioNaoEncontrado(String identificador) {
        throw new UsuarioException(String.format("Usuário com identificador '%s' não encontrado", identificador), HttpStatus.NOT_FOUND);
    }

    public static UsuarioException usuarioJaCadastrado(String identificador) {
        throw new UsuarioException(String.format("Usuário com chip '%s' já está cadastrado", identificador), HttpStatus.CONFLICT);
    }

    public static UsuarioException ongNaoEncontrada() {
        throw new UsuarioException("Organização não encontrada. Entre em contato com o suporte.", HttpStatus.NOT_FOUND);
    }

    public static void usuarioErroFazerLogin(String mensagemErro) {
        throw new UsuarioException(mensagemErro, HttpStatus.UNAUTHORIZED);
    }

}
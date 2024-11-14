package org.ong.pet.pex.backendpetx.service.exceptions.usuarioException;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UsuarioException extends RuntimeException {

    private final Map<String, String> erros;

    public UsuarioException(String mensagem) {
        super(mensagem);
        this.erros = new HashMap<>();
    }

    public UsuarioException(String mensagem, Map<String, String> erros) {
        super(mensagem);
        this.erros = erros;
    }

    public static UsuarioException recursoNaoEncontrado(String nomeRecurso, Object identificador) {
        throw new UsuarioException(String.format("%s com ID '%s' não encontrado", nomeRecurso, identificador));
    }

    public static UsuarioException jaExiste(String nomeRecurso,String campoErro, Object identificador) {
        throw new UsuarioException(String.format("%s com %s '%s' já existe", nomeRecurso,campoErro, identificador));
    }

    public static UsuarioException dadosInvalidos(Map<String, String> erros) {
        throw new UsuarioException("Dados inválidos", erros);
    }

    public static UsuarioException usuarioNaoEncontrado(String identificador) {
        throw new UsuarioException(String.format("Usuário com identificador '%s' não encontrado", identificador));
    }

    public static UsuarioException usuarioJaCadastrado(String identificador) {
        throw new UsuarioException(String.format("Usuário com chip '%s' já está cadastrado", identificador));
    }

    public static UsuarioException ongNaoEncontrada() {
        throw new UsuarioException("Organização não encontrada. Entre em contato com o suporte.");
    }

    public static void usuarioErroFazerLogin(String mensagemErro) {
        throw new UsuarioException(mensagemErro);
    }

}
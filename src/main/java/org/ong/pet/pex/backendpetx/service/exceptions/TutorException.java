package org.ong.pet.pex.backendpetx.service.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TutorException extends RuntimeException {

    private final Map<String, String> erros;
    private final HttpStatus status; // Adiciona o status HTTP

    public TutorException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.erros = new HashMap<>();
        this.status = status;
    }

    public TutorException(String mensagem, Map<String, String> erros) {
        super(mensagem);
        this.erros = erros;
        this.status = HttpStatus.BAD_REQUEST; // Default para erros de validação
    }

    // Métodos estáticos para diferentes tipos de exceções
    public static TutorException recursoNaoEncontrado(String nomeRecurso, Object identificador) {
        return new TutorException(String.format("%s com ID '%s' não encontrado", nomeRecurso, identificador), HttpStatus.NOT_FOUND);
    }

    public static TutorException jaExiste(String nomeRecurso, String campoErro, Object identificador) {
        return new TutorException(String.format("%s com %s '%s' já existe", nomeRecurso, campoErro, identificador), HttpStatus.CONFLICT);
    }

    public static TutorException dadosInvalidos(Map<String, String> erros) {
        return new TutorException("Dados inválidos", erros);
    }

    public static TutorException tutorNaoEncontrado(String identificador) {
        return new TutorException(String.format("Tutor com identificador '%s' não encontrado", identificador), HttpStatus.NOT_FOUND);
    }

    public static TutorException tutorJaCadastrado(String identificador) {
        return new TutorException(String.format("Tutor com identificador '%s' já está cadastrado", identificador), HttpStatus.CONFLICT);
    }

    public static TutorException tutorInativo(String identificador) {
        return new TutorException(String.format("Tutor com identificador '%s' está inativo no sistema", identificador), HttpStatus.GONE);
    }

    public static TutorException permissaoNegada() {
        return new TutorException("Permissão negada para realizar esta operação.", HttpStatus.FORBIDDEN);
    }
}

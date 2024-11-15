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
        throw new TutorException(String.format("%s com identificador '%s' não encontrado", nomeRecurso, identificador), HttpStatus.NOT_FOUND);
    }

    public static TutorException jaExiste(String nomeRecurso, String campoErro, Object identificador) {
        throw new TutorException(String.format("%s com %s '%s' já existe", nomeRecurso, campoErro, identificador), HttpStatus.CONFLICT);
    }

    public static TutorException dadosInvalidos(Map<String, String> erros) {
        throw new TutorException("Dados inválidos", erros);
    }

    public static TutorException tutorNaoEncontrado(String identificador) {
        throw new TutorException(String.format("Tutor com identificador '%s' não encontrado", identificador), HttpStatus.NOT_FOUND);
    }

    public static TutorException tutorJaCadastrado(String identificador) {
        throw new TutorException(String.format("Tutor com identificador '%s' já está cadastrado", identificador), HttpStatus.CONFLICT);
    }

    public static TutorException tutorInativo(String identificador) {
        throw new TutorException(String.format("Tutor com identificador '%s' está inativo no sistema", identificador), HttpStatus.GONE);
    }

    public static TutorException permissaoNegada() {
        throw new TutorException("Permissão negada para realizar esta operação.", HttpStatus.FORBIDDEN);
    }

    public static TutorException cpfNaoPodeSerVazioOuNulo() {
        throw new TutorException("Cpf não pode estar em branco ou nulo", HttpStatus.BAD_REQUEST);
    }

    public static Exception naoHaTutoresCadastrados() {
        throw new TutorException("Não há tutores cadastrados!", HttpStatus.BAD_REQUEST);
    }
}

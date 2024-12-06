package org.ong.pet.pex.backendpetx.service.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PetXException extends RuntimeException {

    private final Map<String, String> erros;
    private final HttpStatus status; // Adiciona o status

    public PetXException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.erros = new HashMap<>();
        this.status = status;
    }

    public PetXException(String mensagem) {
        super(mensagem);
        this.erros = new HashMap<>();
        this.status = HttpStatus.BAD_REQUEST;
    }

    public PetXException(String mensagem, Map<String, String> erros) {
        super(mensagem);
        this.erros = erros;
        this.status = HttpStatus.BAD_REQUEST; // Default para erros de validação
    }

    // Métodos estáticos ajustados para usar o novo construtor com status
    public static PetXException recursoNaoEncontrado(String nomeRecurso, Object identificador) {
        return new PetXException(String.format("%s com ID '%s' não encontrado", nomeRecurso, identificador), HttpStatus.NOT_FOUND);
    }

    public static PetXException jaExiste(String nomeRecurso, String campoErro, Object identificador) {
        return new PetXException(String.format("%s com %s '%s' já existe", nomeRecurso, campoErro, identificador), HttpStatus.CONFLICT);
    }

    public static PetXException dadosInvalidos(Map<String, String> erros) {
        return new PetXException("Dados inválidos", erros);
    }

    public static PetXException animalNaoEncontrado(String chip) {
        return new PetXException(String.format("Animal com identificador '%s' não encontrado", chip), HttpStatus.NOT_FOUND);
    }

    public static PetXException animalJaCadastrado(String chip) {
        return new PetXException(String.format("Animal com chip '%s' já está cadastrado", chip), HttpStatus.CONFLICT);
    }

    public static PetXException ongNaoEncontrada() {
        return new PetXException("Organização não encontrada. Entre em contato com o suporte.", HttpStatus.NOT_FOUND);
    }

    public static PetXException animalJaFalecido(String chip) {
        return new PetXException(String.format("Animal com chip '%s' já consta como falecido", chip), HttpStatus.GONE);
    }

    public static PetXException jaExisteAnimalComChipId(String id) {
        return new PetXException(String.format("Animal com o chip '%s' já existente!", id), HttpStatus.NOT_FOUND);
    }
}

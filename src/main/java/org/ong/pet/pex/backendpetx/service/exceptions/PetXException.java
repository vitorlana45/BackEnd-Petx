package org.ong.pet.pex.backendpetx.service.exceptions;

import lombok.Getter;
import org.ong.pet.pex.backendpetx.controllers.exceptions.setup.BaseApplicationError;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PetXException extends BaseApplicationError {

    private final Map<String, String> erros;
    private final HttpStatus status; // Adiciona o status

    public PetXException(String mensagem, HttpStatus status) {
        super(status, mensagem, null);
        this.erros = new HashMap<>();
        this.status = status;
    }

    public PetXException(String mensagem) {
        super(HttpStatus.BAD_REQUEST, mensagem,null);
        this.erros = new HashMap<>();
        this.status = HttpStatus.BAD_REQUEST;
    }

    // Métodos estáticos ajustados para usar o novo construtor com status
    public static PetXException recursoNaoEncontrado(String nomeRecurso, Object identificador) {
        return new PetXException(String.format("%s com ID '%s' não encontrado", nomeRecurso, identificador), HttpStatus.NOT_FOUND);
    }


    public static PetXException animalNaoEncontrado(String chip) {
        return new PetXException(String.format("Animal com identificador '%s' não encontrado", chip), HttpStatus.NOT_FOUND);
    }

    public static PetXException animalJaCadastrado(String id) {
        return new PetXException(String.format("%s", id), HttpStatus.CONFLICT);
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

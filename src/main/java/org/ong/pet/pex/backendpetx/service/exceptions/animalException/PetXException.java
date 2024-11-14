package org.ong.pet.pex.backendpetx.service.exceptions.animalException;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PetXException extends RuntimeException {

    private final Map<String, String> erros;

    public PetXException(String mensagem) {
        super(mensagem);
        this.erros = new HashMap<>();
    }

    public PetXException(String mensagem, Map<String, String> erros) {
        super(mensagem);
        this.erros = erros;
    }

    public static PetXException recursoNaoEncontrado(String nomeRecurso, Object identificador) {
        return new PetXException(String.format("%s com ID '%s' não encontrado", nomeRecurso, identificador));
    }

    public static PetXException jaExiste(String nomeRecurso,String campoErro, Object identificador) {
        return new PetXException(String.format("%s com %s '%s' já existe", nomeRecurso,campoErro, identificador));
    }

    public static PetXException dadosInvalidos(Map<String, String> erros) {
        return new PetXException("Dados inválidos", erros);
    }

    public static PetXException animalNaoEncontrado(String chip) {
        return new PetXException(String.format("Animal com identificador '%s' não encontrado", chip));
    }

    public static PetXException animalJaCadastrado(String chip) {
        return new PetXException(String.format("Animal com chip '%s' já está cadastrado", chip));
    }

    public static PetXException ongNaoEncontrada() {
        return new PetXException("Organização não encontrada. Entre em contato com o suporte.");
    }

    public static PetXException animalJaFalecido(String chip) {
        return new PetXException(String.format("Animal com chip '%s' já consta como falecido", chip));
    }
}
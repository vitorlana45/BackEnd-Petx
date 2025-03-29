package org.ong.pet.pex.backendpetx.enums;

public enum Destino {

    ABRIGO("Abrigo"),
    ADOTACAO("Adoção"),
    DEVOLUCAO_TUTOR("Devolução Tutor"),
    COMUNITARIO("Comunitário"),
    OUTRO("Outro");

    private final String descricao;

    Destino(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

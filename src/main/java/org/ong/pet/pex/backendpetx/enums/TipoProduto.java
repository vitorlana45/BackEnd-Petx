package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum TipoProduto {
    RACAO("Ração"),
    ALIMENTACAO("Alimentação"),
    MEDICAMENTO("Medicamento"),
    HIGIENE("Higiene"),
    LIMPEZA("Limpeza"),
    ACESSORIO("Acessório"),
    BRINQUEDO("Brinquedo"),
    UTENSILIOS("Utensílios"),
    OUTRO("Outro");

    private final String descricao;

    TipoProduto(String descricao) {
        this.descricao = descricao;
    }
}
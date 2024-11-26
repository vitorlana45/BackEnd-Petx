package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum MaturidadeEnum {
    FILHOTE("FILHOTE"),
    ADULTO("ADULTO"),
    IDOSO("IDOSO");

    private final String maturidade;

    MaturidadeEnum(String maturidade) {
        this.maturidade = maturidade;
    }
}

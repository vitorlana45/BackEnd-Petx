package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum CategoriaDespesaEnum {
    ONG("ONG"),
    ANIMAL("ANIMAL"),
    MEDICAMENTO("MEDICAMENTO"),
    RACAO("RACAO");

    private final String despesa;

    CategoriaDespesaEnum(String despesa) {
        this.despesa = despesa;
    }
}
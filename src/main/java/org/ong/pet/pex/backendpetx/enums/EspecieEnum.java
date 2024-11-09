package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum EspecieEnum {
    GATO("GATO"),
    CACHORRO("CACHORRO");
    private final String especie;

    EspecieEnum(String especie) {
        this.especie = especie;
    }
}

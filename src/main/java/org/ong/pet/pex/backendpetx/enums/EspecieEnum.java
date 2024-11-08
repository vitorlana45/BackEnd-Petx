package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum EspecieEnum {
    Gato("gato"),
    Cachorro("cachorro");
    private final String especie;

    EspecieEnum(String especie) {
        this.especie = especie;
    }
}

package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum SexoEnum {
    MACHO("MACHO"),
    FEMEA("FEMEA");

    private final String sexo;

    SexoEnum(String sexo) {
        this.sexo = sexo;
    }
}

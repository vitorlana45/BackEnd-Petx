package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum SexoEnum {
    Macho("macho"),
    Femea("femea");

    private final String sexo;

    SexoEnum(String sexo) {
        this.sexo = sexo;
    }
}

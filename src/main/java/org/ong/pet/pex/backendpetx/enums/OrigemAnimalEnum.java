package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum OrigemAnimalEnum {
    GUARDA_MUNICIPAL("GUARDA_MUNICIPAL"),
    ABANDONO("ABANDONO"),
    RESGATE("RESGATE");
    private final String OrigemAnimal;

    OrigemAnimalEnum(String origemAnimal) {
        OrigemAnimal = origemAnimal;
    }
}

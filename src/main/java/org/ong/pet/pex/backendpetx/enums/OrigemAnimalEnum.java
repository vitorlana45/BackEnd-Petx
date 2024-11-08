package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum OrigemAnimalEnum {
    Guarda_Municipal("Guarda_Municipal"),
    Abandono("Abandono"),
    Resgate("Resgate");
    private final String OrigemAnimal;

    OrigemAnimalEnum(String origemAnimal) {
        OrigemAnimal = origemAnimal;
    }
}

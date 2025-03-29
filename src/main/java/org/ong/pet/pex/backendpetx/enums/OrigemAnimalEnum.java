package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum OrigemAnimalEnum {
    GUARDA_MUNICIPAL("GUARDA_MUNICIPAL"),
    ABANDONO("ABANDONO"),
    RESGATE("RESGATE"),
    SMA("SMA"),
    DIRETORIA("DIRETORIA"),
    COMUNIDADE("COMUNIDADE"),
    FUNCIONARIO("FUNCIONARIO"),
    PORTAO("PORTAO"),
    OUTROS("OUTROS");

    private final String OrigemAnimal;

    OrigemAnimalEnum(String origemAnimal) {
        OrigemAnimal = origemAnimal;
    }
}

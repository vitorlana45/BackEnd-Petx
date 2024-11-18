package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum UnidadeDeMedida {
    QUILO("QUILO"),
    LITRO("LITRO"),
    UNIDADE("UNIDADE");


    private final String unidadeDeMedida;

    UnidadeDeMedida(String unidadeDeMedida) {
        this.unidadeDeMedida = unidadeDeMedida;
    }

}

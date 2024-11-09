package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum PorteEnum {
    PEQUENO("PEQUENO"),
    MEDIO("MEDIO"),
    GRANDE("GRANDE");
    private final String porte;

    PorteEnum(String porte) {
        this.porte = porte;
    }
}

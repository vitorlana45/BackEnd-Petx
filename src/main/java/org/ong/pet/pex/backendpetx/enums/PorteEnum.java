package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum PorteEnum {
    Pequeno("pequeno"),
    Medio("medio"),
    Grande("grande");
    private final String porte;

    PorteEnum(String porte) {
        this.porte = porte;
    }
}

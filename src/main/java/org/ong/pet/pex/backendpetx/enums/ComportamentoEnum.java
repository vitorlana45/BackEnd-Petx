package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum ComportamentoEnum {
    AGRESSIVO("AGRESSIVO"),
    DOCIL("DOCIL");
    private final String comportamento;

    ComportamentoEnum(String comportamento) {
        this.comportamento = comportamento;
    }
}

package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum ComportamentoEnum {
    Agressivo("agressivo"),
    Docil("docil");
    private final String comportamento;

    ComportamentoEnum(String comportamento) {
        this.comportamento = comportamento;
    }
}

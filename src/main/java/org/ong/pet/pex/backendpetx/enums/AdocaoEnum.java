package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum AdocaoEnum {
    DISPONIVEL("DISPONIVEL"),
    ADOTADO("ADOTADO");

    private final String adocao;

    AdocaoEnum(String adocao) {
        this.adocao = adocao;
    }

}

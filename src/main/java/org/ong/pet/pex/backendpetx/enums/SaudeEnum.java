package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum SaudeEnum {

    DOENTE("DOENTE"),
    SAUDAVEL("SAUDAVEL");
//    ADOTADO("ADOTADO"),
//    FALECIDO("FALECIDO");
//    ADOTADO_CONJUNTAMENTE("ADOTADO_CONJUNTAMENTE");

    private final String status;

    SaudeEnum(String status) {
        this.status = status;
    }
}
package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    DOENTE("DOENTE"),
    SAUDAVEL("SAUDAVEL"),
    ADOTADO("ADOTADO"),
    FALECIDO("FALECIDO"),
    ADOTADO_CONJUNTAMENTE("ADOTADO_CONJUNTAMENTE");

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }
}
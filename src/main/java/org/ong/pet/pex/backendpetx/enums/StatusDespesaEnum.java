package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum StatusDespesaEnum {

    PENDENTE("PENDENTE"),
    PAGO("PAGO"),
    ATRASADA("ATRASADA");

    private String status;

    StatusDespesaEnum(String status) {
        this.status = status;
    }
}
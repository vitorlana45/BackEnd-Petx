package org.ong.pet.pex.backendpetx.enums;

import lombok.Getter;

@Getter
public enum FormaPagamentoEnum {

    CREDITO("CREDITO"),
    DEBITO("DEBITO"),
    PIX("PIX"),
    BOLETO("BOLETO"),
    OUTROS("OUTROS");

    private final String formaPagamento;

    FormaPagamentoEnum(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

}
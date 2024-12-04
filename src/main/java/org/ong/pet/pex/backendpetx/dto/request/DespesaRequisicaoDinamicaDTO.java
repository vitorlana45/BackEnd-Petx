package org.ong.pet.pex.backendpetx.dto.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record DespesaRequisicaoDinamicaDTO(

        String descricao,

        BigDecimal valor,

        String categoria,

        LocalDate dataPrevistaPagamento,

        String formaPagamento,

        String statusDespesa,

        LocalDate dataPagamento
) { }
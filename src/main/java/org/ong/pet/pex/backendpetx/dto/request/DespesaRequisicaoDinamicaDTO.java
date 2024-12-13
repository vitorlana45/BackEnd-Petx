package org.ong.pet.pex.backendpetx.dto.request;

import lombok.Builder;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record DespesaRequisicaoDinamicaDTO(

        String descricao,

        BigDecimal valor,

        CategoriaDespesaEnum categoria,

        LocalDate dataPrevistaPagamento,

        FormaPagamentoEnum formaPagamento,

        StatusDespesaEnum statusDespesa,

        LocalDate dataPagamento
) { }
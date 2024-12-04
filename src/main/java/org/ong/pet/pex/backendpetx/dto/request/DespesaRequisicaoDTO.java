package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record DespesaRequisicaoDTO(

        String descricao,

        @NotNull(message = "Valor é obrigatorio")
        BigDecimal valor,

        @NotNull(message = "Categoria é obrigatoria")
        CategoriaDespesaEnum categoria,

        @NotNull(message = "Data prevista de pagamento é obrigatoria")
        LocalDate dataPrevistaPagamento,

        @NotNull(message = "Forma de pagamento é obrigatoria")
        FormaPagamentoEnum formaPagamento,

        @NotNull(message = "Status da despesa é obrigatorio")
        StatusDespesaEnum statusDespesa,

        LocalDate dataPagamento
) { }
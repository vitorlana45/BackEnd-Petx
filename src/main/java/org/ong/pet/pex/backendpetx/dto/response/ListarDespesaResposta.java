package org.ong.pet.pex.backendpetx.dto.response;

import lombok.Builder;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ListarDespesaResposta(
        Long id,
        CategoriaDespesaEnum categoria,
        String descricao,
        BigDecimal valor,
        LocalDate dataPrevistaPagamento,
        FormaPagamentoEnum formaPagamento,
        LocalDate dataPagamento,
        StatusDespesaEnum statusDespesa
) {
}

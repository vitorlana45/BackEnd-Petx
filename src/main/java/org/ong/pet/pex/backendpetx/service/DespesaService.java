package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.DespesaRequisicaoDinamica;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.DespesaResposta;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;
import org.ong.pet.pex.backendpetx.entity.Despesa;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DespesaService {

    DespesaResposta cadastrarDespesa(final DespesaRequisicao despesaRequestDTO);

    void deletarDespesa(final Long id);

    DespesaResposta atualizarDespesa(final Long id, final DespesaRequisicao despesaRequestDTO);

    Page<ListarDespesaResposta> paginarDespesa(
            String descricao,
            CategoriaDespesaEnum categoria,
            StatusDespesaEnum status,
            FormaPagamentoEnum formaPagamento,
            LocalDate dataPagamento,
            LocalDate dataPrevistaPagamento,
            BigDecimal valor,
            Pageable pageable
    );

    DespesaResposta atualizarDespesaDinamicamente(Long id, DespesaRequisicaoDinamica dto);

    Despesa buscarDespesaPorId(Long id);
}
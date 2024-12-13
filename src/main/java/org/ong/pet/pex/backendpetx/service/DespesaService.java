package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDTO;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDinamicaDTO;
import org.ong.pet.pex.backendpetx.dto.response.DespesaDTORespota;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DespesaService {

    DespesaDTORespota cadastrarDespesa(final DespesaRequisicaoDTO despesaRequestDTO);

    void deletarDespesa(final Long id);

    DespesaDTORespota atualizarDespesa(final Long id, final DespesaRequisicaoDTO despesaRequestDTO);

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

    DespesaDTORespota atualizarDespesaDinamicamente(Long id, DespesaRequisicaoDinamicaDTO dto);
}
package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDTO;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDinamicaDTO;
import org.ong.pet.pex.backendpetx.dto.response.DespesaDTORespota;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;

import java.util.List;

public interface DespesaService {

    DespesaDTORespota cadastrarDespesa(final DespesaRequisicaoDTO despesaRequestDTO);

    void deletarDespesa(final Long id);

    DespesaDTORespota atualizarDespesa(final Long id, final DespesaRequisicaoDTO despesaRequestDTO);

    List<ListarDespesaResposta> listarDespesa();

    List<ListarDespesaResposta> listarDespesaFiltrada(String filtro);

    DespesaDTORespota atualizarDespesaDinamicamente(Long id, DespesaRequisicaoDinamicaDTO dto);
}
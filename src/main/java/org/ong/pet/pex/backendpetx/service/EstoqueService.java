package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.response.EstoqueResponseDTO;
import org.ong.pet.pex.backendpetx.dto.response.ListarEstoqueResponse;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;

public interface EstoqueService {

    EstoqueResponseDTO pegarEstoquePorId(final Long id);
    RacaoDisponivelResposta calcularQuantidadeRacao();
    ListarEstoqueResponse listarEstoque();
    ListarEstoqueResponse listarEstoquePorTipoProduto(String tipoProduto);
}

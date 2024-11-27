package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.response.ListarEstoqueResponse;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.entities.Estoque;

public interface EstoqueService {

    Estoque pegarEstoquePorId();
    RacaoDisponivelResposta calcularQuantidadeRacao();
    ListarEstoqueResponse listarEstoque();
}

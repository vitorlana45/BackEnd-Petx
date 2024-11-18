package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.CadastrarEstoqueRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.entities.Estoque;

import java.util.List;

public interface EstoqueService {

    void salvarEstoque(CadastrarEstoqueRequisicao cadastrarEstoqueRequisicao);
    Estoque deletar(Long id);
    List<Estoque> GetAll();
    Estoque update(Long id, Estoque estoque);
    RacaoDisponivelResposta calcularQuantidadeRacao();
}

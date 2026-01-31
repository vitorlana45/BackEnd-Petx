package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.controllers.estoque.EstoqueDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.entities.Estoque;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstoqueService {

    RacaoDisponivelResposta calcularQuantidadeRacao();
    Page<ProdutoDTOResposta> paginarProdutoEstoque(TipoProduto tipoProduto, String nome, Double quantidade, UnidadeDeMedidaEnum medida, String chave, Pageable pageable);

    // Novos métodos para o cadastro de estoque
    Estoque criarEstoque(EstoqueDTO estoqueDTO);
    Estoque atualizarEstoque(EstoqueDTO estoqueDTO);
    Page<ProdutoDTOResposta> listarProdutosPorEstoque(Long estoqueId, String nome, Pageable pageable);
}

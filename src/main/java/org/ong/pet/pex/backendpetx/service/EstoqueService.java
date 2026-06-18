package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.EstoqueDTO;
import org.ong.pet.pex.backendpetx.dto.produto.ProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoResposta;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.entity.Estoque;
import org.ong.pet.pex.backendpetx.entity.Produto;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstoqueService {

    RacaoDisponivelResposta calcularQuantidadeRacao();
    Page<ProdutoResposta> paginarProdutoEstoque(TipoProduto tipoProduto, String nome, Double quantidade, UnidadeDeMedidaEnum medida, String chave, Pageable pageable);

    // Métodos para o cadastro de estoque
    Estoque criarEstoque(EstoqueDTO estoqueDTO);
    Estoque atualizarEstoque(EstoqueDTO estoqueDTO);
    Page<ProdutoResposta> listarProdutosPorEstoque(Long estoqueId, String nome, Pageable pageable);

    // Método para salvar produto
    Produto salvarProduto(ProdutoDTO produtoDTO);
}

package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;
import org.ong.pet.pex.backendpetx.entities.Produto;

import java.util.List;

public interface ProdutoService {
    Long cadastrarProduto(ProdutoDTO produtoDTO);
    List<ProdutoDTOResposta> listarProdutos();
    Produto buscarProdutoPorId(Long id);
    ProdutoDTOResposta atualizarProduto(Long id, ProdutoDTO produtoAtualizado);
    void deletarProduto(Long id);
}
package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;

import java.util.List;

public interface ProdutoService {
    Long cadastrarProduto(ProdutoDTO produtoDTO);
    List<ProdutoDTOResposta> listarProdutos();
    ProdutoDTOResposta buscarProdutoPorId(Long id);
    ProdutoDTOResposta atualizarProduto(Long id, ProdutoDTO produtoAtualizado);
    void deletarProduto(Long id);
}
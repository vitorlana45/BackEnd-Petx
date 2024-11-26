package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.entities.Produto;

import java.util.List;

public interface ProdutoService {
    void cadastrarProduto(ProdutoDTO produtoDTO);
    List<ProdutoDTO> listarProdutos();
    Produto buscarProdutoPorId(Long id);
    ProdutoDTO atualizarProduto(Long id, ProdutoDTO produtoAtualizado);
    void excluirProduto(Long id);
}
package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;

import java.util.List;


public interface ProdutoService {

    Long cadastrarProduto(ProdutoDTO dto);

    List<ProdutoDTOResposta> listarProdutos();

    ProdutoDTOResposta buscarProdutoPorId(Long id);

    ProdutoDTOResposta atualizarProduto(Long id, ProdutoDTO dto);

    void deletarProduto(Long id);
}

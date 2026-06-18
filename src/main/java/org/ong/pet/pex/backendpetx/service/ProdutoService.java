package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoResposta;


public interface ProdutoService {

    Long cadastrarProduto(ProdutoDTO dto);

    ProdutoResposta buscarProdutoPorId(Long id);

    ProdutoResposta atualizarProduto(Long id, ProdutoDTO dto);

    void deletarProduto(Long id);
}

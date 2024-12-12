package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstoqueService {

    RacaoDisponivelResposta calcularQuantidadeRacao();
    Page<ProdutoDTOResposta> paginarProdutoEstoque(TipoProduto tipoProduto, String nome, Double quantidade, UnidadeDeMedidaEnum medida, String chave, Pageable pageable);

}

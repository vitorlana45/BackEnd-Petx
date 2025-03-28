package org.ong.pet.pex.backendpetx.dto.response;


import lombok.Builder;
import org.ong.pet.pex.backendpetx.dto.request.InfoProdutoDTO;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;

import java.util.List;

@Builder
public record ProdutoDTOResposta (

        Long produtoId,

        TipoProduto tipoProduto,

        String nome,

        String descricao,

        Double quantidade,

        UnidadeDeMedidaEnum unidadeDeMedida,

        List<InfoProdutoDTO> atrubutosEspecificos

) {

}

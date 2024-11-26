package org.ong.pet.pex.backendpetx.dto.request;

import lombok.Builder;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;

import java.util.List;

@Builder
public record ProdutoDTO(
        TipoProduto tipoProduto,
        String nome,
        String descricao,
        Long quantidade,
        UnidadeDeMedidaEnum unidadeDeMedida,
        List<InfoProdutoDTO> metaData
) {
}
package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;

import java.util.List;

@Builder
public record ProdutoDTO(
        @NotNull(message = "Tipo do produto não pode ser nulo")
        TipoProduto tipoProduto,
        String nome,
        String descricao,
        Long quantidade,
        UnidadeDeMedidaEnum unidadeDeMedida,
        List<InfoProdutoDTO> atributosDinamicos
) {
}
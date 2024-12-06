package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;

import java.util.List;

@Builder
public record ProdutoDTO(

        @NotNull(message = "Tipo do produto não pode ser nulo")
        TipoProduto tipoProduto,

        @NotBlank(message = "Nome do produto não pode ser vazio")
        String nome,

        String descricao,

        @NotNull(message = "Preço do produto não pode ser nulo")
        Double quantidade,

        @NotNull(message = "Preço do produto não pode ser nulo")
        UnidadeDeMedidaEnum unidadeDeMedida,

        List<InfoProdutoDTO> atrubutosEspecificos
) {
}
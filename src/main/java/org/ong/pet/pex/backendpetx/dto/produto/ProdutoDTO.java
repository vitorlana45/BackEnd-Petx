package org.ong.pet.pex.backendpetx.dto.produto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ong.pet.pex.backendpetx.entities.CategoriaEstoque;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {

    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private Double quantidade;

    @NotNull(message = "A unidade de medida é obrigatória")
    private UnidadeDeMedidaEnum unidadeDeMedida;

    @NotNull(message = "A categoria é obrigatória")
    private CategoriaEstoque categoriaEstoque;

    private Long estoqueId;

    private Map<String, String> atributosEspecificos = new HashMap<>();
}

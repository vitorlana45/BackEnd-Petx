package org.ong.pet.pex.backendpetx.dto.categoria_estoque;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCategoriaEstoqueRequest {

    @NotNull(message = "ID é obrigatório")
    private Long id;
}

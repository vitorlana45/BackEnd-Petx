package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotNull;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;

public record ConsumoAlimentoRequisicao(

        @NotNull(message = "O campo porte é obrigatório")
        PorteEnum porte,
        @NotNull(message = "O campo consumoDiario é obrigatório")
        Double consumoDiario
) {

}

package org.ong.pet.pex.backendpetx.dto.request;

import lombok.Builder;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;

@Builder
public record CadastrarEstoqueRequisicao(
        String racao,
        Double quantidade,
        EspecieEnum especie,
        PorteEnum porte
) {
}

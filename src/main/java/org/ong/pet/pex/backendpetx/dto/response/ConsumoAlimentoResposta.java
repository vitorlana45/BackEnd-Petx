package org.ong.pet.pex.backendpetx.dto.response;

import org.ong.pet.pex.backendpetx.enums.PorteEnum;

public record ConsumoAlimentoResposta(
        Long id,
        PorteEnum porte,
        Double consumoDiario
) {


}

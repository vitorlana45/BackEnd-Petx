package org.ong.pet.pex.backendpetx.dto.response;

import lombok.Builder;

@Builder
public record RacaoDisponivelResposta(
        long quantidadeRacaoEmGramas,
        double consumoDiraio,
        String racaoDias
) {
}

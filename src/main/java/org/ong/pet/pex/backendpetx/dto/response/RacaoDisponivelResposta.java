package org.ong.pet.pex.backendpetx.dto.response;

import lombok.Builder;

@Builder
public record RacaoDisponivelResposta(
        double quantidadeRacaoEmGramas,
        double consumoDiraio,
        String racaoDias
) {
}

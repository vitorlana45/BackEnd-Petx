package org.ong.pet.pex.backendpetx.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ListarEstoqueResponse (

        Long id,
        LocalDateTime craidoEm,
        LocalDateTime atualizadoEm,
        List<ProdutoDTOResposta> produtos


){
}

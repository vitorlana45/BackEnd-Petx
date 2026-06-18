package org.ong.pet.pex.backendpetx.dto.response;

import java.util.List;

public record EstoqueResponseDTO(
        Long id,
        String criadoEm,
        String atualizadoEm,
        List<ProdutoDTOResposta> produtos
        ) {
}
